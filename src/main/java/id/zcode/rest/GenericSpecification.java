package id.zcode.rest;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericSpecification<T> implements Specification<T> {

    private SpecSearchCriteria criteria;

    public GenericSpecification(final SpecSearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public GenericSpecification() {
        super();
        this.criteria = new SpecSearchCriteria();
    }

    @Nullable
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        Expression<String> expression = null;
        if (criteria.getValue() instanceof String) {
            Join joiner = null;
            String[] keys = criteria.getKey().split("_");
            for (int i = 0; i < keys.length - 1; i++) {
                if (joiner == null) joiner = root.join(keys[i], JoinType.INNER);
                else joiner = joiner.join(keys[i], JoinType.INNER);
            }
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            Matcher matcher = pattern.matcher(criteria.getValue().toString());
            expression = joiner != null ? joiner.get(keys[keys.length - 1])
                    : root.get(criteria.getKey());
            if (matcher.find()) expression = builder.lower(expression);
        }
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(expression, criteria.getValue());
            case NEGATION:
                return builder.notEqual(expression, criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(expression, criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(expression, criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(expression, "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(expression, "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}
