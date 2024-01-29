package exercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;
import org.springframework.transaction.annotation.Transactional;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    private Task generateTask() {
        String para = faker.lorem().paragraph();
        if(para.length() > 255)
            para = para.substring(0, 255);
        String finalPara = para;
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> finalPara)
                .create();
    }

    // BEGIN
    private Task testTask;

    @BeforeEach
    public void setUp() {
        testTask = generateTask();
        testTask = taskRepository.save(testTask);
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(testTask)));
    }

    @Test
    public void testCreate() throws Exception {

        Task testTask1 = generateTask();

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask1));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {

        testTask.setTitle(faker.lorem().word());
        testTask.setDescription(faker.lorem().paragraph());

        var request = put("/tasks/{id}", testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualTask = taskRepository.findById(testTask.getId()).get();

        assertThat(actualTask.getTitle()).isEqualTo(testTask.getTitle());
        assertThat(actualTask.getDescription()).isEqualTo(testTask.getDescription());
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk());

        assertThat(taskRepository.findAll()).isEmpty();
    }

    // END
}
