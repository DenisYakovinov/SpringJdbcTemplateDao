package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDaoConfig;
import img.imaginary.exception.DaoException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@TestInstance(Lifecycle.PER_CLASS)
class CommonDaoTest {

    @Autowired
    GroupDao groupDaoImpl;

    @Autowired
    AudienceDao audienceDaoImpl;

    @Autowired
    StudentDao studentDaoImpl;

    @Autowired
    SubjectDao subjectDaoImpl;

    @Autowired
    TeacherDao teacherDaoImpl;

    @Autowired
    TimetableClassDao timetableLineDaoImpl;

    @ParameterizedTest
    @MethodSource("daoImplsProvider")
    void findById_ShouldThrowDaoException_WhenEntityNotFound(GenericDao<?, Integer> daoImpl) {
        assertThrows(DaoException.class, () -> daoImpl.findById(0));
    }
    
    @Sql(scripts = "/dropAllTables.sql")
    @Sql(scripts = "/testTablesCreation.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    @ParameterizedTest
    @MethodSource("daoImplsProvider")
    void findAll_ShouldThrowDaoException_WhenTablesNotExist(GenericDao<?, Integer> daoImpl) {
        assertThrows(DaoException.class, () -> daoImpl.findAll());
    }
    
    Stream<GenericDao<?, Integer>> daoImplsProvider() {
        return Stream.of(groupDaoImpl, audienceDaoImpl, studentDaoImpl, subjectDaoImpl, teacherDaoImpl,
                timetableLineDaoImpl);
    }
}
