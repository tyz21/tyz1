package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

    /**
     * Во входном файле храниться информация о системе главных автодорог,
     * связывающих г.Полоцк с другими городами Беларуси. Используя эту
     * информацию, потсроить дерево, отображающее систему дорог республики, а
     * затем продвигаясь по дереву, определить Миниманльный по длинне путь
     * из г.Полоцк в другой заданный город. Предусмотреть возможность сохранения
     * дерева в виртуальной памяти
     */
    public class Road {
        public static void main(String[] args) {
            //Стою дерево на хэш-мапе
            Map<String, Map<String, Integer>> roadMap = makeMap(new File("data\\road.txt"));
            //вывожу
            System.out.println(roadMap);
            //город до которого нужно дабраться
            String p = "Пинск";
            //вызываю метод
            System.out.println(findWay("Кол", roadMap));
        }

        /**
         * Определение пути
         * @param city Город к которому нужно найти путь
         * @param roadMap карта
         * @return расстояние
         */
        public static int findWay(String city, Map<String, Map<String, Integer>> roadMap) {
            Map<String, Integer> pMap = roadMap.get("Минск");//Здесь город
            int way = 0;
            if (finIfInLine(city, pMap) != 0) {
                way = finIfInLine(city, pMap);
                return way;
            }
            if (getDeep(city, pMap, roadMap) != 0) {
                way = getDeep(city, pMap, roadMap);
                return way;
            }
            for(Map.Entry<String, Integer> entry : pMap.entrySet()) {
                if(!roadMap.containsKey(entry.getKey())) {
                    continue;
                }
                way += pMap.get(entry.getKey());
                Map<String, Integer> innerMap = roadMap.get(entry.getKey());
                if(getDeep(city, innerMap, roadMap) != 0) {
                    way += getDeep(city,innerMap,roadMap);
                    return way;
                }
            }
            return 0;
        }

        public static int getDeep(String city, Map<String, Integer> pMap, Map<String, Map<String, Integer>> roadMap) {
            int way = 0;
            for (Map.Entry<String, Integer> entry : pMap.entrySet()) {
                if (roadMap.containsKey(entry.getKey())) {
                    way += entry.getValue();
                    Map<String, Integer> currentMap = roadMap.get(entry.getKey());
                    if (currentMap.containsKey(city)) {
                        way += currentMap.get(city);
                        return way;
                    } else {
                        way -= entry.getValue();
                    }
                }
            }
            return way;
        }

        public static int finIfInLine(String city, Map<String, Integer> map) {
            if (!map.containsKey(city)) {
                return 0;
            }
            return map.get(city);
        }

        /**
         * Делает дерево из данных файла
         * @param file файл
         * @return дерево
         */
        public static Map<String, Map<String, Integer>> makeMap(File file) {
            Map<String, Map<String, Integer>> roadMap = new LinkedHashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                //Пока не наступит конец файла
                while ((line = reader.readLine()) != null) {
                    //Сканнер по каждой строке
                    Scanner scanner = new Scanner(line);
                    //Задаю разделители
                    scanner.useDelimiter("\\p{Punct}\\s+");
                    //Строку города
                    String city = scanner.next();
                    Map<String, Integer> liks = new LinkedHashMap<>();
                    while (scanner.hasNext()) {
                        String link = scanner.next();
                        String[] splited = link.split("-");
                        //записываю дорогу в коллецию
                        liks.put(splited[0],Integer.parseInt(splited[1]));
                    }
                    //записываю одну коллекцию в другую
                    roadMap.put(city,liks);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return roadMap;
        }
    }

