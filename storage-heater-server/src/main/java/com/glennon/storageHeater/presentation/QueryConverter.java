package com.glennon.storageHeater.presentation;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gerald on 05/06/2017.
 */
public class QueryConverter {

    private static final String QUERY_REGEX = "(\\w*)(=|!=|~=)(\\w*)";
    private static final String EQUALS = "=";
    private static final String NOT_EQUALS = "!=";
    private static final String LIKE = "~=";
    private static final String LIKE_CHAR = ".*";

    private QueryConverter() {
        //not needed
    }

    public static Query convert(String queryParam) {
        Query query = new Query();

        Pattern pattern = Pattern.compile(QUERY_REGEX);
        Matcher matcher = pattern.matcher(queryParam);
        while (matcher.find()) {
            Criteria criteria = getCriteria(matcher);
            if (criteria != null) {
                query.addCriteria(criteria);
            }
        }

        return query;
    }

    private static Criteria getCriteria(Matcher match) {
        if (match.group(2).equals(EQUALS)) {
            return Criteria.where(match.group(1)).is(match.group(3));
        } else if (match.group(2).equals(NOT_EQUALS)) {
            return Criteria.where(match.group(1)).ne(match.group(3));
        } else if (match.group(2).equals(LIKE)) {
            return Criteria.where(match.group(1)).regex(LIKE_CHAR + match.group(3) + LIKE_CHAR);
        }

        return null;
    }
}
