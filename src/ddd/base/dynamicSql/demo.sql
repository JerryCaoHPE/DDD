 [reportViews]    
select EId, inputCode, operatorCode, operateDate, orgId,
  remark, reportViewName, reportViewKey, reportSql, finalSql,
  dataSourceId, reportViewDesc, categoryId from reportView

[reportViews.displayattributes]
SELECT EId, inputCode, operatorCode, operateDate, orgId,
  remark, columnIndex, attributeName, attributeValue,
  cssArrtribute, showType, reportViewId
  FROM displayattribute 
  where reportViewId in (${reportViews.EId})


[reportViews.viewTreeConditions]
SELECT EId, inputCode, operatorCode, operateDate, orgId,
  remark, viewTreeKey, viewTreeName, displayIndex, reportViewId
  FROM viewtreecondition
  where  reportViewId in ( ${reportViews.EId})

 [reportViews.viewTreeConditions.viewTreeNode]
 SELECT vtn.EId, vtn.inputCode, vtn.operatorCode, vtn.operateDate, vtn.orgId,
  vtn.remark, vtn.nodeIndex, vtn.idColumn, vtn.titleColumn, vtn.viewTreeId,
   vtn.issHierachical,  vtn.hierColumn,
  vtn.isLoad, vtn.icon, vtn.template,vtc.EId as viewTreeConditionEid
  FROM viewTreeNode vtn  left join viewTree vt on vtn.viewTreeId = vt.EId 
 left join  viewTreeCondition vtc  on vtc.viewTreeKey = vt.viewTreeKey
 where vtc.EId in ( ${viewTreeConditions.EId})