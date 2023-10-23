package com.example.csv;

import com.example.csv.exception.FutureDateException;
import com.example.csv.model.Employee;
import com.example.csv.service.impl.CsvReaderImpl;
import com.example.csv.service.impl.DateParserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {

    @InjectMocks
    private CsvReaderImpl csvReader;

    @Mock
    private DateParserServiceImpl dateParserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadCsv_ValidFile() throws IOException {
        //Test with a standard file
        Integer employeeId = 199;
        Integer projectId = 15;
        List<Employee> result = csvReader.readCsvFileByName("validFile.csv");
        Optional<Employee> employeeWithId199 = result.stream().filter(employee -> Objects.equals(employee.getEmployeeId(), employeeId)).findFirst();

        assertTrue(employeeWithId199.isPresent());
        assertEquals(employeeWithId199.get().getEmployeeId(), employeeId);
        assertEquals(employeeWithId199.get().getProjectId(), projectId);
        assertEquals(23, result.size());
        assertEquals(143, result.get(0).getEmployeeId());
        assertEquals(12, result.get(0).getProjectId());
    }

    @Test
    public void testReadCsv_EmptyFile() throws IOException {
        //Test with empty file
        List<Employee> result = csvReader.readCsvFileByName("emptyFile.csv");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReadCsv_NullDateDefaultsToNow() throws IOException {
        //Test are the null dates are converted to current dates
        List<Employee> result = csvReader.readCsvFileByName("nullDateToInstantNow.csv");
        assertEquals(result.size(), 1);
        Optional<Employee> firstEmployeeOptional = result.stream().findFirst();
        assertTrue(firstEmployeeOptional.isPresent());

        Employee employee = firstEmployeeOptional.get();
        assertNotNull(employee.getDateTo());
        assertNotNull(employee.getDateFrom());
    }

    @Test
    public void testReadCsv_BadCsvFile() {
        // This test proves that if we have incorrect delimiters in the file it will fail
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvReader.readCsvFileByName("badCsvFile.csv"));
    }

    @Test
    public void testReadCsv_DateInFuture() {
        // Create an instance of DateParserService
        dateParserService = new DateParserServiceImpl();

        // Inject DateParserService into CsvReader
        csvReader = new CsvReaderImpl(dateParserService);
        assertThrows(FutureDateException.class, () -> csvReader.readCsvFileByName("dateInFuture.csv"));
    }

}
