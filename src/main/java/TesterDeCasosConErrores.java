import java.io.*;
import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import semantic.symbol.SymbolTable;

@RunWith(Parameterized.class)
public class TesterDeCasosConErrores {
    
    //TODO: el tipo de esta variable init tiene que ser la clase que tiene el main
    private static final Main init = null;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static final String testFilesDirectoryPath = "resources/conErrores/";
    
     
    @Before
    public  void setUpClass() {
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public  void tearDownClass() {
        System.setOut(originalOut);
    }
    
    @Parameters(name = "{0}")
    public static Iterable<? extends Object> data() {
        File folder = new File(testFilesDirectoryPath);
        ArrayList<String> names = new ArrayList();
        for(File f: folder.listFiles()){
            names.add(f.getName());
        }
        names.sort(String::compareTo);
        return names;
    }
    
    private String input;
    
    public TesterDeCasosConErrores(String input){
        this.input = input;
    }
       
        
    @Test
    public void test1() {
        SymbolTable.getInstance().reset();
        probarFallo(input);
    }

    private void probarFallo(String name) {
        String testCaseFilePath = testFilesDirectoryPath+name;
        String errorCode = getErrorCode(testCaseFilePath);
        String[] args = {testCaseFilePath};
        init.main(args);

        String result = outContent.toString();
        originalOut.println(result);

        assertThat("No se encontro el codigo: " + errorCode,  result, CoreMatchers.containsString(errorCode));
        if (!errorCode.equals("[SinErrores]")){
            assertThat("Compilacion válida cuando hubo error.", result, CoreMatchers.not(CoreMatchers.containsString("[SinErrores]")));
        }
    }


    String getErrorCode(String testCaseFilePath)  {
        String lineWithTheCode = null;
        try {
            lineWithTheCode = (new BufferedReader(new FileReader(testCaseFilePath))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String errorCode = lineWithTheCode.substring(3);
        return errorCode;
    }





    

    
    
    
    
}
