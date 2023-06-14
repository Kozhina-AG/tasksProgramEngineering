package com.example.model;
/**
 * Класс Patient представляет сущность пациента с информацией о его характеристиках.
 */
public class Patient {
    public int age;
    public String sex;
    private int id;
    private String name;
    private String snils;
    private double weight;
    private double height;

    /**
     * Конструктор класса Patient.
     *
     * @param name    имя пациента
     * @param snils   СНИЛС (Страховой номер индивидуального лицевого счета) пациента
     * @param weight  вес пациента
     * @param height  рост пациента
     * @param age     возраст пациента
     * @param sex     пол пациента
     */

    public Patient(String name, String snils, double weight, double height, int age, String sex) {
        this.name = name;
        this.snils = snils;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.sex = sex;
    }

    public Patient(int id, String name, String snils, double weight, double height, int age, String sex) {
        this.id = id;
        this.name = name;
        this.snils = snils;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.sex = sex;
    }
    /**
     * Возвращает идентификатор пациента.
     *
     * @return идентификатор пациента
     */
    public int getId() {
        return id;
    }
    /**
     * Устанавливает идентификатор пациента.
     *
     * @param id идентификатор пациента
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Возвращает имя пациента.
     *
     * @return имя пациента
     */
    public String getName() {
        return name;
    }
    /**
     * Устанавливает имя пациента.
     *
     * @param name имя пациента
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Возвращает СНИЛС пациента.
     *
     * @return СНИЛС пациента
     */
    public String getSnils() {
        return snils;
    }
    /**
     * Устанавливает СНИЛС пациента.
     *
     * @param snils СНИЛС пациента
     */
    public void setSnils(String snils) {
        this.snils = snils;
    }
    /**
     * Возвращает вес пациента.
     *
     * @return вес пациента
     */
    public double getWeight() {
        return weight;
    }
    /**
     * Устанавливает вес пациента.
     *
     * @param weight вес пациента
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
    /**
     * Возвращает рост пациента.
     *
     * @return рост пациента
     */
    public double getHeight() {
        return height;
    }
    /**
     * Устанавливает рост пациента.
     *
     * @param height рост пациента
     */
    public void setHeight(double height) {
        this.height = height;
    }
    /**
     * Возвращает возраст пациента.
     *
     * @return возраст пациента
     */
    public int getAge() {
        return age;
    }
    /**
     * Устанавливает возраст пациента.
     *
     * @param age возраст пациента
     */
    public void setAge(int age) {
        this.age = age;
    }
    /**
     * Возвращает пол пациента.
     *
     * @return пол пациента
     */
    public String getSex() {
        return sex;
    }
    /**
     * Устанавливает пол пациента.
     *
     * @param sex пол пациента
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
}
