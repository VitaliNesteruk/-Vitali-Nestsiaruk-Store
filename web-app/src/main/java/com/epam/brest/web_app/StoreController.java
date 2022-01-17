package com.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StoreController {

    /**
     * Goto store list page
     *
     * @return view name
     */
    @GetMapping(value = "/store")
    public final String store(Model model) {
        return "store";
    }

    /**
     * Goto edit product page
     *
     * @return view name
     */
    @GetMapping(value = "/product/{id}")
    public final String gotoEditStorePage(@PathVariable Integer id, Model model) {
        return "product";
    }

    /**
     * Goto new product page
     *
     * @return view name
     */
    @GetMapping(value = "/product/add")
    public final String gotoAddProductPage(Model model) {
        return "product";
    }
}
