package  ddd.simple.action.codeGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import ddd.base.Config;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.generator.CodeGenerator;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.FileUtil;
import ddd.develop.ProjectCompile;
import ddd.simple.entity.permission.Module;
import ddd.simple.entity.permission.Permission;
import ddd.simple.service.listview.ListViewService;
import ddd.simple.service.permission.ModuleService;
import ddd.simple.service.permission.PermissionService;
import ddd.simple.util.RouteMerge;

@Action
@RequestMapping("/CodeGenerator")
@Controller
public class CodeGeneratorAction {
	@Resource(name="permissionServiceBean")
	private PermissionService permissionService;
	
	@Resource(name="moduleServiceBean")
	private ModuleService moduleService;
	
	@Resource(name="listViewServiceBean")
	private ListViewService listViewService;
	
	
	public Map<String,String> generatorEntity(String classInfo){
		try {
			return  CodeGenerator.generatorEntity(classInfo);
		} catch (DDDException e) {
			throw e;
		}
	}
	public int importEntities(){
		try {
			/*new ExportAndImportUtil().importDataFromFile(
					"D:\\angular\\workspace\\DDD3\\src\\ddd\\simple\\action\\util\\exportAndImport\\masterPlate.txt", 
					"D:\\angular\\workspace\\DDD3\\src\\ddd\\simple\\action\\util\\exportAndImport\\dataFile.xls");*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public Map<String,String> mergeRoute(){
		Map<String,String> result = new HashMap<String, String>();
		result.put("successMsg", "");
		result.put("errorMsg", "");
		try {
			RouteMerge.merge("src\\ddd\\develop\\mergeConfig\\ddd\\routerList.txt","src\\ddd\\develop\\mergeConfig\\ddd\\whiteRouterList.txt","WebContent\\ddd\\extends\\js\\appRouter.js");
			ProjectCompile.merge();
			result.put("successMsg", "合并成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errorMsg", "合并失败，请处理后台错误");
		}
		return result;
	}
	
	public Map<String,String> generatorBaseCode(String className) throws Exception{
		try {
			Map<String,String> result = new Generator().generatorBaseCode(className);
			
			if(!result.get("successMsg").equals("")){
				EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(className));
				String entityNameLow=entityClass.getClassName().substring(0,1).toLowerCase()+entityClass.getClassName().substring(1);
				String lable = entityClass.getEntityInfo().getLabel();
				Module module = null;
				EntitySet<Module> modules = moduleService.findModules("route='main.list."+entityNameLow+"."+entityNameLow+"ListRoute'");
				EntitySet<Permission> dbPermissions=null;
				if(modules.size()==0){
					Module parentModule = new Module();
					parentModule.setEId(1L);
					module = new Module();
					module.setParent(parentModule);
					module.setName(lable+"管理");
					module.setModuleType("否");
					module.setIsInUse("是");
					module.setRoute("main.list."+entityNameLow+"."+entityNameLow+"ListRoute");
					moduleService.saveModule(module);
					
				}else{
					module = modules.iterator().next();
					dbPermissions = permissionService.findPermissionByModule(module.getEId());
				}
				EntitySet<Permission> permissions =createPermission(module, lable, entityNameLow,dbPermissions);
				permissionService.savePermissions(permissions);
				
				//应该在此处生成模块的权限点文件
				JSONObject json = new JSONObject();
				json.put("route", "main.list."+entityNameLow+"."+entityNameLow+"ListRoute");
				json.put("name", lable+"管理");
				json.put("reference",null);
				json.put("permissionMap", this.dealPermissionMap(permissions, entityClass.getClassName()));
				String temp = className.replace("."+entityNameLow.substring(0,1).toUpperCase()+entityNameLow.substring(1)+"","");
				String permissionMapFilePath = Config.applicationPath+"\\src\\"+temp.replace(".","\\\\")+"\\"+entityNameLow+"PermissionMap.json";
				FileUtil.writeToFile(new File(permissionMapFilePath), json.toString());
				//合并路由
				RouteMerge.merge("src\\ddd\\develop\\mergeConfig\\ddd\\routerList.txt","src\\ddd\\develop\\mergeConfig\\ddd\\whiteRouterList.txt","WebContent\\ddd\\extends\\js\\appRouter.js");
				ProjectCompile.merge();
				this.listViewService.createDataSourceAndReportView(className);
			}
			return result;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	private List<JSONObject> dealPermissionMap(EntitySet<Permission> permissions,String entityName){
		List<JSONObject> result = new ArrayList<JSONObject>();	
		Iterator<Permission> ite = permissions.iterator();
		while(ite.hasNext()){
			PermissionMap permissionMap = new PermissionMap();
			Permission temp = ite.next();
			String name = temp.getCode();
			//permissionMap.setpermissionCode(temp.getCode());
			permissionMap.setPermissionName(temp.getName());
			List<String> actionPaths = new ArrayList<String>();
			if(name.contains("_add")){
				actionPaths.add("../"+entityName+"/save"+entityName);
			}else{
				if(name.contains("_delete")){
					actionPaths.add("../"+entityName+"/delete"+entityName);
				}else{
					if(name.contains("_edit")){
						actionPaths.add("../"+entityName+"/update"+entityName);
					}else{
						if(name.contains("_display")){
							actionPaths.add("../"+entityName+"/find"+entityName+"ById");
							actionPaths.add("../"+entityName+"/findAll"+entityName);
						}
					}
				}
			}
			permissionMap.setActionPaths(actionPaths);
			JSONObject jsob = new JSONObject();
			jsob.put(temp.getCode(), permissionMap);
			result.add(jsob);
		}
		return result;
	}
	

	private EntitySet<Permission> createPermission(Module module,String lable,String entityNameLow,EntitySet<Permission> dbPermissions) throws Exception{
		EntitySet<Permission> permissions = new EntitySet<Permission>();
		
		String add_Name = entityNameLow+"_add";
		if(!existPermission(dbPermissions, add_Name)){
			Permission permission_add = new Permission();
			permission_add.setName("新增"+lable);
			permission_add.setCode(add_Name);
			permission_add.setModule(module);
			permissions.add(permission_add);
		}
		
		String delete_Name = entityNameLow+"_delete";
		if(!existPermission(dbPermissions, delete_Name)){
			Permission permission_delete = new Permission();
			permission_delete.setName("删除"+lable);
			permission_delete.setCode(delete_Name);
			permission_delete.setModule(module);
			permissions.add(permission_delete);
		}
		
		String edit_Name = entityNameLow+"_edit";
		if(!existPermission(dbPermissions, edit_Name)){
			Permission permission_edit = new Permission();
			permission_edit.setName("编辑"+lable);
			permission_edit.setCode(edit_Name);
			permission_edit.setModule(module);
			permissions.add(permission_edit);
		}
		
		String display_Name = entityNameLow+"_display";
		if(!existPermission(dbPermissions, display_Name)){
			Permission permission_view = new Permission();
			permission_view.setName("查看"+lable);
			permission_view.setCode(display_Name);
			permission_view.setModule(module);
			permissions.add(permission_view);
		}
		return permissions;
	}
	
	private boolean existPermission(EntitySet<Permission> permissions,String permissionName){
		if(permissions==null){
			return false;
		}
		for (Permission permission : permissions) {
			if(permissionName.equals(permission.getCode())){
				return true;
			}
		}
		return false;
	}
	
}
