package ru.itmo.nemat.lab5.managers;

import ru.itmo.nemat.lab5.models.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Stack;
import java.util.Collection;

import static java.lang.Long.max;

/**
 * Менеджер коллекции. Отвечает за хранение, обработку и выполнение операций над коллекцией драконов.
 *
 * @author Nemati213
 */
public class CollectionManager {
    private final DumpManager dumpManager;
    private Stack<Dragon> collection = new Stack<>();
    private Date lastInitTime;
    private Date lastSaveTime;

    private Long lastID;

    /**
     * Конструктор менеджера коллекции.
     * Выполняет загрузку данных из файла и инициализацию счетчика ID.
     * @param dumpManager Менеджер дампов для работы с файловой системой.
     */
    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
        lastID = 100000L;
        loadCollection();
        if (!collection.isEmpty()) {
            for (var tmp : collection)
                lastID = max(lastID, tmp.getId());
        }
    }

    /**
     * Создает нового дракона и добавляет его в коллекцию.
     * @param name        Имя дракона.
     * @param coordinates Координаты дракона.
     * @param age         Возраст дракона.
     * @param color       Цвет дракона.
     * @param type        Тип дракона.
     * @param character   Характер дракона.
     * @param cave        Пещера дракона.
     */
    public void addToCollection(String name, Coordinates coordinates, Long age,
                                Color color, DragonType type,
                                DragonCharacter character, DragonCave cave) {

        Long newId = generateNextId();
        Date newDate = new Date();

        Dragon newDragon = new Dragon(newId, name,
                coordinates, newDate,
                age, color, type,
                character, cave);
        collection.push(newDragon);
    }

    /**
     * Удаляет конкретный объект дракона из коллекции.
     * @param dragon Объект дракона для удаления.
     */
    public void removeFromCollection(Dragon dragon) {
        collection.remove(dragon);
    }

    /**
     * Удаляет последний элемент из коллекции (верхушку стека).
     * @return true, если элемент был удален, иначе false.
     */
    public boolean removeLast() {
        if (collection.isEmpty()) {
            return false;
        }
        collection.pop();
        return true;
    }

    /**
     * Полностью очищает коллекцию.
     */
    public void clearCollection() {
        collection.clear();
    }

    /**
     * Сохраняет текущую коллекцию в файл.
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = new Date();
    }

    /**
     * Загружает коллекцию из файла.
     */
    public void loadCollection() {
        this.collection = dumpManager.readCollection();
        this.lastInitTime = new Date();
    }

    /**
     * Генерирует следующий уникальный идентификатор для нового элемента.
     * @return Новый идентификатор.
     */
    public Long generateNextId() {
        return ++lastID;
    }

    /**
     * Ищет дракона в коллекции по его идентификатору.
     * @param id Идентификатор дракона.
     * @return Объект дракона, если найден, иначе null.
     */
    public Dragon getByID(Long id) {
        for (Dragon dragon : collection) {
            if (dragon.getId().equals(id)) {
                return dragon;
            }
        }
        return null;
    }

    /**
     * Проверяет существование дракона в коллекции по его идентификатору.
     * @param id Идентификатор дракона.
     * @return true, если дракон существует, иначе false.
     */
    public boolean isExist(Long id) {
        return getByID(id) != null;
    }

    /**
     * @return Текущий стек коллекции.
     */
    public Stack<Dragon> getCollection() {
        return collection;
    }

    /**
     * @return Дата и время последней инициализации коллекции.
     */
    public Date getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Дата и время последнего сохранения коллекции.
     */
    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Тип используемой коллекции в виде строки.
     */
    public String collectionType() {
        return collection.getClass().getName();
    }

    /**
     * @return Количество элементов в коллекции.
     */
    public int collectionSize() {
        return collection.size();
    }

    /**
     * Сортирует коллекцию в естественном порядке.
     */
    public void sortCollection() {
        collection.sort(null);
    }

}