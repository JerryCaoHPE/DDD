package ddd.ddd3test.action.ddd3TestModel;

import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.ddd3test.entity.ddd3TestModel.Ddd3Test;
import ddd.ddd3test.service.ddd3TestModel.Ddd3TestService;
import ddd.simple.entity.codeTable.CodeType;

@Action
@RequestMapping("/DD/Ddd3Test")
@Controller
public class Ddd3TestAction
{
	@Resource(name="ddd3TestServiceBean")
	private Ddd3TestService ddd3TestService;
	
	public Ddd3Test saveDdd3Test(Ddd3Test ddd3Test)
	{
		try {
			Ddd3Test saveDdd3Test = this.ddd3TestService.saveDdd3Test(ddd3Test);
			return saveDdd3Test;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteDdd3Test(Long ddd3TestId){
		
		try {
			return this.ddd3TestService.deleteDdd3Test(ddd3TestId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public Ddd3Test updateDdd3Test(Ddd3Test ddd3Test) {
		try {
			Ddd3Test updateDdd3Test = this.ddd3TestService.updateDdd3Test(ddd3Test);
			return updateDdd3Test;
		} catch (DDDException e) {
			throw e;
		}
	}

	public Ddd3Test findDdd3TestById(Long ddd3TestId){
		try {
			Ddd3Test findDdd3Test = this.ddd3TestService.findDdd3TestById(ddd3TestId);
			findDdd3Test.getOrganization().getName();
			EntitySet<CodeType> codeTypes = findDdd3Test.getCodeTypes();
			Iterator<CodeType> ite = codeTypes.iterator();
			while(ite.hasNext()){
				ite.next().getName();
			}
			return  findDdd3Test;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<Ddd3Test> findAllDdd3Test(){
		try{
			EntitySet<Ddd3Test> allDdd3Test = this.ddd3TestService.findAllDdd3Test();
			return allDdd3Test;
		} catch (DDDException e) {
			throw e;
		}
	}

}