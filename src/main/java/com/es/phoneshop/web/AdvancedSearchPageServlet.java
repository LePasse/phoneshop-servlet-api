package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SearchType;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        request.setAttribute("searchTypes", SearchType.getSearchTypes());
        Long minPrice = getAndVerifyPrice(request, errors, "minPrice", "Min price is not a valid number.");
        Long maxPrice = getAndVerifyPrice(request, errors, "maxPrice", "Max price is not a valid number.");
        String searchType = getAndVerifySearchType(request, errors);
        if (errors.isEmpty()) {
            String query = request.getParameter("query");
            request.setAttribute("products", productDao.searchProducts(query, minPrice, maxPrice, searchType != null ? SearchType.valueOf(searchType) : SearchType.ALL_WORDS));
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("products", new ArrayList<>());
        }
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private Long getAndVerifyPrice(HttpServletRequest request, Map<String, String> errors, String parameterName, String errorMessage) {
        String priceString = request.getParameter(parameterName);
        Long priceValue = null;
        try {
            if (priceString != null && !priceString.trim().isEmpty()) {
                priceValue = Long.valueOf(priceString);
            }
        } catch (NumberFormatException e) {
            errors.put(parameterName, errorMessage);
        }
        return priceValue;
    }

    private String getAndVerifySearchType(HttpServletRequest request, Map<String, String> errors) {
        String searchType = request.getParameter("searchType");
        if (searchType == null || !(searchType.equals("ALL_WORDS") || searchType.equals("ANY_WORD"))){
            errors.put("searchType", "Search type is invalid");
        }
        return searchType;
    }
}
