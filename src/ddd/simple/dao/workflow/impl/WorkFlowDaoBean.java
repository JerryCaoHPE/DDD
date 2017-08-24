package ddd.simple.dao.workflow.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.workflow.WorkFlowDao;

@Service
public class WorkFlowDaoBean extends BaseDao implements WorkFlowDao 
{
	final Logger logger = LoggerFactory.getLogger(CheckOptionDaoBean.class);
}
