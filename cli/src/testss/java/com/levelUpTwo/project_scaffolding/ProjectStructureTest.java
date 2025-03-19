package com.levelUpTwo.project_scaffolding;

import com.scaffoldcli.zapp.zapp.ServerAccess.ServerAccessHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProjectStructureTest {

    @Test
    void testGetScaffOptionsWithInvalidJson() {
    // Arrange: Define invalid mock JSON
    String scaffId = "12345";
    String invalidJsonResponse = "{ \"invalidJson\": \"data\" }"; // Not expected structure

    // Mock the ServerAccessHandler call to return invalid JSON string
    when(serverAccessHandler.getScaffServerRequest(scaffId + "/options")).thenReturn(invalidJsonResponse);

    // Act: Call the method to test
    Map<String, String> result = ProjectStructure.getScaffOptions(scaffId);

    // Assert: Verify that the result is empty or defaults to an expected value
    assertThat(result).isNotNull();
    assertThat(result).isEmpty(); // If invalid, the result should be empty
    }

    @Test
    void testCreateFilesFromComplexJson() throws IOException {
    // Arrange: Define a complex mock JSON structure
    String mockJsonResponse = "{\n" +
            "\"files\": {\n" +
            "\"file1.txt\": \"content1\",\n" +
            "\"folder1\": {\n" +
            "\"file2.txt\": \"content2\"\n" +
            "},\n" +
            "\"folder2\": {\n" +
            "\"file3.txt\": \"content3\"\n" +
            "}\n" +
            "}\n" +
            "}";

    // Mock the Files.write method
    Files mockFiles = mock(Files.class);
    doNothing().when(mockFiles).write(any(), any());

    // Act: Call the method to test
    ProjectStructure.createFilesFromJson(mockJsonResponse);

    // Assert: Verify file creation
    // Verify the file creation for the root folder and nested folders
    verify(mockFiles, times(1)).write(Paths.get("MyProj" + File.separator + "file1.txt"), "content1".getBytes());
    verify(mockFiles, times(1)).write(Paths.get("MyProj" + File.separator + "folder1" + File.separator + "file2.txt"), "content2".getBytes());
    verify(mockFiles, times(1)).write(Paths.get("MyProj" + File.separator + "folder2" + File.separator + "file3.txt"), "content3".getBytes());
    }

    @Test
    void testExecuteFinalScaffWithValidJson() {
    // Arrange: Mock JSON responses for both the options and the final scaffold rendering
    String scaffId = "12345";
    String scaffOptionsJson = "{\"1\": {\"name\": \"Option1\"}, \"2\": {\"name\": \"Option2\"}}";
    String scaffRenderedJson = "{\"files\": {\n" +
            "\"file1.txt\": \"content1\",\n" +
            "\"folder1\": {\n" +
            "\"file2.txt\": \"content2\"\n" +
            "}\n" +
            "}}";

    // Mock the ServerAccessHandler responses
    when(serverAccessHandler.getScaffServerRequest(scaffId + "/options")).thenReturn(scaffOptionsJson);
    when(serverAccessHandler.getScaffServerRequest(scaffId + "/rendered")).thenReturn(scaffRenderedJson);

    // Act: Run the executeFinalScaff method
    ProjectStructure.executeFinalScaff(scaffId);

    // Assert: Verify the flow of calls and file creation
    verify(serverAccessHandler, times(1)).getScaffServerRequest(scaffId + "/options");
    verify(serverAccessHandler, times(1)).getScaffServerRequest(scaffId + "/rendered");

    // Verify file creation
    verify(Files.class, times(1)).write(Paths.get("MyProj" + File.separator + "file1.txt"), "content1".getBytes());
    verify(Files.class, times(1)).write(Paths.get("MyProj" + File.separator + "folder1" + File.separator + "file2.txt"), "content2".getBytes());
    }

    @Test
    void testGetScaffOptionsWithServerError() {
    // Arrange: Simulate a server error response
    String scaffId = "12345";
    when(serverAccessHandler.getScaffServerRequest(scaffId + "/options")).thenThrow(new RuntimeException("Server error"));

    // Act: Call the method and expect an exception or error handling
    try {
        ProjectStructure.getScaffOptions(scaffId);
        fail("Expected exception due to server failure");
    } catch (RuntimeException e) {
        // Assert: Verify that the error is handled (or logged) properly
        assertThat(e).hasMessageContaining("Server error");
    }
    }

    @Test
    void testGetScaffOptionsWithNullResponse() {
    // Arrange: Define a null response from the server
    String scaffId = "12345";
    when(serverAccessHandler.getScaffServerRequest(scaffId + "/options")).thenReturn(null);

    // Act: Call the method with a null response
    Map<String, String> result = ProjectStructure.getScaffOptions(scaffId);

    // Assert: Verify the result is handled gracefully (empty map or default)
    assertThat(result).isNotNull();
    assertThat(result).isEmpty(); // Should return an empty map if no options are returned
    }

    @Test
    void testFileCreationForNestedFolders() throws IOException {
    // Arrange: Define mock JSON with nested folders and files
    String mockJsonResponse = "{\n" +
            "\"files\": {\n" +
            "\"folder1\": {\n" +
            "\"subfolder1\": {\n" +
            "\"file1.txt\": \"content1\"\n" +
            "}\n" +
            "},\n" +
            "\"folder2\": {\n" +
            "\"file2.txt\": \"content2\"\n" +
            "}\n" +
            "}\n" +
            "}";

    // Mock file writing behavior
    Files mockFiles = mock(Files.class);
    doNothing().when(mockFiles).write(any(), any());

    // Act: Run the file creation method
    ProjectStructure.createFilesFromJson(mockJsonResponse);

    // Assert: Verify that files are created in the correct nested paths
    verify(mockFiles, times(1)).write(Paths.get("MyProj" + File.separator + "folder1" + File.separator + "subfolder1" + File.separator + "file1.txt"), "content1".getBytes());
    verify(mockFiles, times(1)).write(Paths.get("MyProj" + File.separator + "folder2" + File.separator + "file2.txt"), "content2".getBytes());
    }



}
