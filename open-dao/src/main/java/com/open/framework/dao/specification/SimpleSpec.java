package com.open.framework.dao.specification;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.web.SpecOper;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class SimpleSpec<T> implements Specification<T> {

    /**
     * 查询的条件列表，是一组列表
     */
    private List<Object> opers;

    public SimpleSpec(List<Object> opers) {
        this.opers = opers;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        //通过resultPre来组合多个条件
        String join = BaseConstant.DAO_AND;
        Predicate predicate=null;
        for (Object object : opers) {

            if (object instanceof SpecOper) {
                SpecOper op = (SpecOper) object;
                Predicate pre = generatePredicate(root, criteriaBuilder, op);
                if (null != pre && null!=predicate) {
                    predicate=criteriaBuilder.and(pre,predicate);
                }else{
                    predicate=criteriaBuilder.and(pre);
                }
            } else if (object instanceof List) {
                List<SpecOper> specOpers = (List) object;
                Predicate[] predicates = new Predicate[specOpers.size()];
                for (int i = 0; i < specOpers.size(); i++) {
                    SpecOper op = specOpers.get(i);
                    if (i == 0) {
                        join = op.getJoin();
                    }
                    predicates[i]=(generatePredicate(root, criteriaBuilder, op));
                }
                if (BaseConstant.DAO_AND.equalsIgnoreCase(join)) {
                    if(null==predicate){
                        predicate=criteriaBuilder.and(predicates);
                    }else{
                        predicate=criteriaBuilder.and(predicate,criteriaBuilder.and(predicates));
                    }
                } else if (BaseConstant.DAO_OR.equalsIgnoreCase(join)) {
                    if(null==predicate){
                        predicate=criteriaBuilder.or(predicates);
                    }else{
                        predicate=criteriaBuilder.and(predicate,criteriaBuilder.or(predicates));
                    }
                }
            }
        }
        return predicate;
    }

    private Predicate generatePredicate(Root<T> root, CriteriaBuilder criteriaBuilder, SpecOper op) {
        Expression exp = root.get(op.getKey());
        Object value = op.getValue();
        String oper = op.getOper();

        /*
         * 根据不同的操作符返回特定的查询*/
        if (BaseConstant.EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.equal(exp, value);
        } else if (BaseConstant.GREATER_OR_EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.ge(exp, (Number) value);
        } else if (BaseConstant.LESS_OR_EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.le(exp, (Number) value);
        } else if (BaseConstant.GREATER.equalsIgnoreCase(oper)) {
            return criteriaBuilder.gt(exp, (Number) value);
        } else if (BaseConstant.LESS.equalsIgnoreCase(oper)) {
            return criteriaBuilder.lt(exp, (Number) value);
        } else if (BaseConstant.LIKE.equalsIgnoreCase(oper)) {
            return criteriaBuilder.like(exp, "%" + value + "%");
        } else if (BaseConstant.LIKEA.equalsIgnoreCase(oper)) {
            return criteriaBuilder.like(exp, value + "%");
        } else if (BaseConstant.LIKEB.equalsIgnoreCase(oper)) {
            return criteriaBuilder.like(exp, "%" + value);
        } else if (BaseConstant.IS_NULL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.isNull(exp);
        } else if (BaseConstant.IS_NOT_NULL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.isNotNull(exp);
        } else if (BaseConstant.NOT_EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.notEqual(exp, value);
        } else if (BaseConstant.NOT_LIKE.equalsIgnoreCase(oper)) {
            return criteriaBuilder.notLike(exp, "%" + value + "%");
        } else if (BaseConstant.IS_EMPTY.equalsIgnoreCase(oper)) {
            return criteriaBuilder.isEmpty(exp);
        } else if (BaseConstant.IS_NOT_EMPTY.equalsIgnoreCase(oper)) {
            return criteriaBuilder.isNotEmpty(exp);
        } else if (BaseConstant.IS_IN.equalsIgnoreCase(oper)) {
            String[] values = value.toString().split(",");
            return criteriaBuilder.and(exp.in(values));
        } else if (BaseConstant.IS_NOT_IN.equalsIgnoreCase(oper)) {
            String[] values = value.toString().split(",");
            return criteriaBuilder.not(exp.in(values));
        }
        return null;
    }

}
