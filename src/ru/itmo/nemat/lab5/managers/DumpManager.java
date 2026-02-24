package ru.itmo.nemat.lab5.managers;

import ru.itmo.nemat.lab5.models.Dragon;

import java.io.*;

import java.lang.reflect.Type;
import java.util.Stack;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.itmo.nemat.lab5.utility.OutputPrinter;


/**
 * Менеджер дампов. Отвечает за сохранение коллекции в файл и её загрузку из файла в формате JSON.
 *
 * @author Nemati213
 */
public class DumpManager {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private final String fileName;
    private final OutputPrinter printer;

    /**
     * Конструктор менеджера дампов.
     * @param variableName Имя переменной окружения, содержащей путь к файлу.
     * @param printer      Инструмент для вывода сообщений.
     */
    public DumpManager(String variableName, OutputPrinter printer) {
        this.fileName = System.getenv(variableName);
        this.printer = printer;

    }


    /**
     * Записывает коллекцию в файл.
     * @param collection Коллекция драконов для сохранения.
     */
    public void writeCollection(Stack<Dragon> collection) {
        if (fileName == null || fileName.isEmpty()) {
            printer.printError("Переменная окружения не найдена!");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(collection, writer);
        } catch (IOException e) {
            printer.printError("Не удалось записать данные в файл!");
        }
    }


    /**
     * Считывает коллекцию из файла.
     * @return Считанная коллекция драконов. В случае ошибки возвращает пустой стек.
     */
    public Stack<Dragon> readCollection() {
        if (fileName != null && !fileName.isEmpty()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName))) {

                Type collectionType = new TypeToken<Stack<Dragon>>() {}.getType();

                Stack<Dragon> collection = gson.fromJson(reader, collectionType);

                return (collection == null) ? new Stack<>() : collection;

            } catch (FileNotFoundException e) {
                printer.printError("Загрузочный файл не найден!");
            } catch (Exception e) {
                printer.printError("Ошибка при загрузке данных: " + e.getMessage());
            }
        } else {
            printer.printError("Переменная окружения с путем к файлу не найдена!");
        }
        return new Stack<>();
    }
}