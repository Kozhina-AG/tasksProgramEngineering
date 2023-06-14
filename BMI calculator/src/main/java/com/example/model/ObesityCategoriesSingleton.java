package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс ObesityCategoriesSingleton отвечает за хранение категорий "ожирения" пациентов
 * и реализует методы для добавления, удаления и получения списка категорий.
 * Класс реализован как Одиночка (Singleton).
 */

public class ObesityCategoriesSingleton {
    private static ObesityCategoriesSingleton instance;
    private final List<String> categories;

    private ObesityCategoriesSingleton() {
        categories = new ArrayList<>();
        categories.add("Значительный дефицит массы тела"); // категория 0 - 16
        categories.add("Дефицит массы тела"); // категория 16 - 18.5
        categories.add("Норма"); // категория 18.5 - 25
        categories.add("Лишний вес"); // категория 25 - 30
        categories.add("Ожирение первой степени"); // категория 30 - 35
        categories.add("Ожирение второй степени"); // категория 35 - 40
        categories.add("Ожирение третьей степени"); // категория 40 - 100
    }

    public static ObesityCategoriesSingleton getInstance() {
        if (instance == null) {
            instance = new ObesityCategoriesSingleton();
        }
        return instance;
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void removeCategory(String category) {
        categories.remove(category);
    }

    public List<String> getCategories() {
        return categories;
    }
}
