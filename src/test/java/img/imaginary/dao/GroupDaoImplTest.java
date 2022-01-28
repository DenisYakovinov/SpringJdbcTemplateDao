package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDataSourceConfig;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/insertTestGroups.sql")
class GroupDaoImplTest {

    @Autowired
    GroupDao groupDaoImpl;

    List<Student> students = Arrays.asList(
            new Student(1, "foo", "bar", 2, LocalDate.of(2019, Month.AUGUST, 28), "foo@bar.com"),
            new Student(2, "Andrew", "Anderson", 3, LocalDate.of(2020, Month.AUGUST, 25), "andrew@and.com"),
            new Student(3, "Jonh", "Doe", 3, LocalDate.of(2020, Month.AUGUST, 23), "jonh@doe.com"));

    @Sql("/InsertTestStudents.sql")
    @SqlMergeMode(MergeMode.MERGE)
    @Test
    void findAll_ShouldReturnAllGroups() {
        Group group = new Group(1, "xx-zz", null, "math sciences");
        group.setStudents(students);
        List<Group> expected = Arrays.asList(group, new Group(2, "zz-cc", new ArrayList<Student>(), "sport"),
                new Group(3, "cc-vv", new ArrayList<Student>(), "humanitarian sciences"));

        assertEquals(expected, groupDaoImpl.findAll());
    }

    @Test
    @Sql("/InsertTestStudents.sql")
    @SqlMergeMode(MergeMode.MERGE)
    void findById_ShouldReturnGroupWithSpecifiedID_WhenGroupId() {
        Group expected = new Group(1, "xx-zz", null, "math sciences");
        expected.setStudents(students);
        assertEquals(expected, groupDaoImpl.findById(1));
    }

    @Sql("/InsertTestStudents.sql")
    @SqlMergeMode(MergeMode.MERGE)
    @Test
    void getStudents_ShouldReturnStudentsLinkedWithGroup_WhenGroupId() {
        List<Student> expected = students;
        assertEquals(expected, groupDaoImpl.getStudents(1));
    }

    @Test
    void findById_ShouldReturnGroupWithSpecifiedIDWithoutStudents_WhenGroupId() {
        Group expected = new Group(1, "xx-zz", new ArrayList<Student>(), "math sciences");
        assertEquals(expected, groupDaoImpl.findById(1));
    }
}