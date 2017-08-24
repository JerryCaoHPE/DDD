package ddd.simple.dao.workflow.impl;

import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.workflow.CheckOptionDao;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CheckOptionDaoBean extends BaseDao implements CheckOptionDao 
{
	final Logger logger = LoggerFactory.getLogger(CheckOptionDaoBean.class);
}
