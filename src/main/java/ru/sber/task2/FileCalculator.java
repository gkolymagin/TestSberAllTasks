package ru.sber.task2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

//Есть файл, в котором построчно располагаются простые примеры типа Число1ЗнакЧисло2. Числа типа long, знаки: +,-,*,/
//Например:
//30*20
//188-53
//160/2
//Нужно вычислить эти значения и записать в другой файл:
//600
//135
//80
//Предусмотреть, что в выражении могут быть умышленные ошибки и его вычислить нельзя
// (10/0, 10/а, 10//10 и пр). В этом случае вывести, что за ошибка в примере.
public class FileCalculator {

    public void readWriteFromInFile(File fileIn, File fileOut){
        Parser parser = new Parser();
        try (Stream<String> stream = Files.lines(Paths.get(fileIn.getCanonicalPath()))) {
            Files.write(Paths.get(fileOut.getCanonicalPath()), (Iterable<String>)stream.map((s) -> {
                try {
                    return parser.parse(s)+"";
                } catch (Exception e) {
                    return e.getMessage();
                }
            })::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileCalculator fileCalculator = new FileCalculator();
        fileCalculator.readWriteFromInFile(new File("./fileIn"),new File("./fileOut"));
    }

}
