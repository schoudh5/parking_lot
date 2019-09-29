import org.junit.Before;
import org.junit.Test;
import services.MemoryParkingService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RunnerTest {
    private final Runner runner = new Runner();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void main_whenEnteredValidFile_thenPerformCommandExecution() {
        String filePath = getClass().getClassLoader().getResource("file_inputs.txt").getPath();
        String[] args = {filePath};
        runner.main(args);
        String actualOutput = outContent.toString();
        String expectedOutput = "Created a parking lot with 6 slots\n" +
                "Allocated slot number: 1\n" +
                "Allocated slot number: 2\n" +
                "Allocated slot number: 3\n" +
                "Allocated slot number: 4\n" +
                "Allocated slot number: 5\n" +
                "Allocated slot number: 6\n" +
                "Slot number 4 is free\n" +
                "Slot No.    Registration No    Colour\n" +
                "1           KA-01-HH-1234      White \n" +
                "2           KA-01-HH-9999      White \n" +
                "3           KA-01-BB-0001      Black \n" +
                "5           KA-01-HH-2701      Blue  \n" +
                "6           KA-01-HH-3141      Black \n" +
                "Allocated slot number: 4\n" +
                "Sorry, parking lot is full\n" +
                "KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333\n" +
                "1, 2, 4\n" +
                "6\n" +
                "Not found\n";
        assertThat(actualOutput, is(expectedOutput));
    }

    @Test
    public void main_whenEnteredInValidFile_thenPerformCommandExecution() {
        String filePath = getClass().getClassLoader().getResource("invalid_file.txt").getPath();
        String[] args = {filePath};
        runner.main(args);
        String actualOutput = outContent.toString();
        String expectedOuput = "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n" +
                "Wrong Command!!! Please review your input\n";
        assertThat(actualOutput, is(expectedOuput));
    }

    @Test
    public void gtname(){
        Class c = MemoryParkingService.class;
        String className = c.getName();
        System.out.println("The fully-qualified name of the class is: " + className);

    }
}