package com.open.framework.dao.specification;

import com.open.framework.commmon.BaseConstant;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;


public class SimpleSpec<T> implements Specification<T> {

    /**
     * 查询的条件列表，是一组列表
     * */
    private List<SpecOper> opers;

    public SimpleSpec(List<SpecOper> opers) {
        this.opers = opers;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        int index = 0;
        //通过resultPre来组合多个条件
        Predicate resultPre = null;
        for(SpecOper op:opers) {
            if(index++==0) {
                resultPre = generatePredicate(root,criteriaBuilder,op);
                continue;
            }
            Predicate pre = generatePredicate(root,criteriaBuilder,op);
            if(pre==null) continue;
            if(BaseConstant.DAO_AND.equalsIgnoreCase(op.getJoin())) {
                resultPre = criteriaBuilder.and(resultPre,pre);
            } else if(BaseConstant.DAO_OR.equalsIgnoreCase(op.getJoin())) {
                resultPre = criteriaBuilder.or(resultPre,pre);
            }
        }
        return resultPre;
    }

    private Predicate generatePredicate(Root<T> root,CriteriaBuilder criteriaBuilder, SpecOper op) {
        /*
        * 根据不同的操作符返回特定的查询*/
        if(BaseConstant.EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.equal(root.get(op.getKey()),op.getValue());
        } else if(BaseConstant.GREATER_OR_EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.ge(root.get(op.getKey()), (Number)op.getValue());
        } else if(BaseConstant.LESS_OR_EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.le(root.get(op.getKey()),(Number)op.getValue());
        } else if(BaseConstant.GREATER.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.gt(root.get(op.getKey()),(Number)op.getValue());
        } else if(BaseConstant.LESS.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.lt(root.get(op.getKey()),(Number)op.getValue());
        } else if(BaseConstant.LIKE.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.like(root.get(op.getKey()),"%"+op.getValue()+"%");
        } else if(BaseConstant.LIKEA.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.like(root.get(op.getKey()),op.getValue()+"%");
        } else if(BaseConstant.LIKEB.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.like(root.get(op.getKey()),"%"+op.getValue());
        } else if(BaseConstant.IS_NULL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.isNull(root.get(op.getKey()));
        } else if(BaseConstant.IS_NOT_NULL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.isNotNull(root.get(op.getKey()));
        } else if(BaseConstant.NOT_EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.notEqual(root.get(op.getKey()),op.getValue());
        } else if(BaseConstant.NOT_LIKE.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.notLike(root.get(op.getKey()),"%"+op.getValue()+"%");
        }else if(BaseConstant.IS_EMPTY.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.isEmpty(root.get(op.getKey()));
        }else if(BaseConstant.IS_NOT_EMPTY.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.isNotEmpty(root.get(op.getKey()));
        }else if(BaseConstant.IS_IN.equalsIgnoreCase(op.getOper())) {
            Expression exp = root.get(op.getKey());
            String[] values=op.getValue().toString().split(",");
            return criteriaBuilder.and(exp.in(values));
        }else if(BaseConstant.IS_NOT_IN.equalsIgnoreCase(op.getOper())) {
            Expression exp = root.get(op.getKey());
            String[] values=op.getValue().toString().split(",");
            return criteriaBuilder.not(exp.in(values));
        }
        return null;
    }

}
