package ddd.base.persistence;

import java.util.LinkedHashSet;

import ddd.base.persistence.baseEntity.Entity;

public class  EntitySet<E extends Entity> extends LinkedHashSet<E> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3032657843991275445L;

	/**
     *添加元素到集合中，如果已经存在，则替换.由于在Entity中重写了hashCode方法，用实体Id作为对象标识，
     *因此，同一个实体在集合只存在一个。如果实体的Id为null,则新实体可以在集合中存在多个
     *
     * @param e 要添加的元素
     * @return <tt>true</tt> 如果元素不存在
     */
    public boolean add(E e) {
        if(super.add(e) == false)
        {
        	super.remove(e);
        	super.add(e);
        	return false;
        }
        else
        {
        	return true;
        }
    }
}
