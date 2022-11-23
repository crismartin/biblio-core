package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous.*;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service // @Profile("dev")
public class DatabaseSeederDev {

    private AuthorDao authorDao;
    private PublisherDao publisherDao;
    private CategoryDao categoryDao;
    private CustomerDao customerDao;
    private BookDao bookDao;
    private LoanBookDao loanBookDao;
    private CopyBookDao copyBookDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev(DatabaseStarting databaseStarting, AuthorDao authorDao,
                             PublisherDao publisherDao, CategoryDao categoryDao,
                             CustomerDao customerDao, BookDao bookDao,
                             LoanBookDao loanBookDao, CopyBookDao copyBookDao) {
        this.databaseStarting = databaseStarting;
        this.authorDao = authorDao;
        this.publisherDao = publisherDao;
        this.categoryDao = categoryDao;
        this.customerDao = customerDao;
        this.bookDao = bookDao;
        this.loanBookDao = loanBookDao;
        this.copyBookDao = copyBookDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    private void deleteAllAndInitialize() {
        LogManager.getLogger(this.getClass()).warn("------- Deleted All - BEGIN -------");

        authorDao.deleteAll();
        publisherDao.deleteAll();
        categoryDao.deleteAll();
        customerDao.deleteAll();
        copyBookDao.deleteAll();
        bookDao.deleteAll();
        loanBookDao.deleteAll();

        LogManager.getLogger(this.getClass()).warn("------- Deleted All - END -------");
        this.databaseStarting.initialize();
    }

    private void seedDataBaseJava() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA - BEGIN --------");
        LogManager.getLogger(this.getClass()).warn("        ------- Authors");

        AuthorEntity[] authors = {
                AuthorEntity.builder().fullName("Ángel Largo García").build(),
                AuthorEntity.builder().fullName("Amador Rivas").build()
        };
        authorDao.saveAll(List.of(authors));

        LogManager.getLogger(this.getClass()).warn("        ------- Publishers");
        PublisherEntity[] publishers = {
                PublisherEntity.builder().name("Editorial de Prueba I").build(),
                PublisherEntity.builder().name("Editorial de Prueba II").build(),
        };
        publisherDao.saveAll(List.of(publishers));

        LogManager.getLogger(this.getClass()).warn("        ------- Categories");
        CategoryEntity[] categories = {
                CategoryEntity.builder().name("Categoria I").build(),
                CategoryEntity.builder().name("Categoria II").build(),
                CategoryEntity.builder().name("Categoria III").build()
        };
        categoryDao.saveAll(List.of(categories));

        LogManager.getLogger(this.getClass()).warn("        ------- Customers");
        CustomerEntity[] customers = {
                CustomerEntity.builder().id("1").identity("77219088S").name("Pepe").surname("Perez")
                        .secondSurname("Sanchez").nick("peperez").numberMembership("11111").build(),
                CustomerEntity.builder().id("2").identity("02735900G").name("Jose").surname("Suarez")
                        .secondSurname("Juarez").nick("josurez").numberMembership("22222").build(),
                CustomerEntity.builder().id("3").identity("41830740L").name("Raul").surname("Lopez")
                        .secondSurname("Mopez").nick("raulez").numberMembership("33333").build(),
                CustomerEntity.builder().id("4").identity("73512923R").name("Antonio").surname("Juarez")
                        .secondSurname("Larez").nick("anrez").numberMembership("44444").build()
        };
        customerDao.saveAll(List.of(customers));

        LogManager.getLogger(this.getClass()).warn("        ------- Books");
        BookEntity[] books = {
                BookEntity.builder().isbn("9788497443869").title("Desdramatizar en la vida y en el trabajo")
                        .releaseDate(LocalDate.now())
                        .summary("Lo que nadie te ha contado sobre como dejar de sufrir y vivir apasionado").authors(List.of(authors))
                        .categories(List.of(categories)).publisher(publishers[0])
                        .build(),
                BookEntity.builder().isbn("9788457089895").title("LIBRO DE PRUEBA 2")
                        .releaseDate(LocalDate.now())
                        .summary("Ejemplo de resumen del libro 2").authors(List.of(authors))
                        .categories(List.of(categories)).publisher(publishers[0])
                        .build(),
                BookEntity.builder().isbn("9788457089870").title("LIBRO DE PRUEBA 3")
                        .releaseDate(LocalDate.now())
                        .summary("Ejemplo de resumen del libro 3").authors(List.of(authors))
                        .categories(List.of(categories)).publisher(publishers[0])
                        .build()
        };
        bookDao.saveAll(List.of(books));

        CopyBookEntity[] copyBooks = {
                CopyBookEntity.builder()
                        .id("1").reference("ref-1").available(false).location("ESTANTERIA")
                        .bookEntity(books[0])
                        .build(),
                CopyBookEntity.builder()
                        .id("2").reference("ref-2").available(true).location("DEPOSITO")
                        .bookEntity(books[0])
                        .build(),
                CopyBookEntity.builder()
                        .id("3").reference("ref-3").available(true).location("DEPOSITO")
                        .bookEntity(books[0])
                        .build(),
                CopyBookEntity.builder()
                        .id("4").reference("ref-4").available(true).location("DEPOSITO")
                        .bookEntity(books[0])
                        .build(),
                CopyBookEntity.builder()
                        .id("5").reference("ref-5").available(true).location("DEPOSITO")
                        .bookEntity(books[0])
                        .build(),
                CopyBookEntity.builder()
                        .id("6").reference("ref-6").available(true).location("DEPOSITO")
                        .bookEntity(books[1])
                        .build(),
                CopyBookEntity.builder()
                        .id("7").reference("ref-7").available(true).location("DEPOSITO")
                        .bookEntity(books[1])
                        .build(),
                CopyBookEntity.builder()
                        .id("8").reference("ref-8").available(true).location("DEPOSITO")
                        .bookEntity(books[1])
                        .build()
        };
        copyBookDao.saveAll(List.of(copyBooks));

        LogManager.getLogger(this.getClass()).warn("        ------- Loans");
        LoanBookEntity[] loans = {
          LoanBookEntity.builder()
                  .reference("loanRef-1")
                  .copyBookEntities(List.of(copyBooks[0]))
                  .customerEntity(customers[2])
                  .returned(false)
                  .startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1))
                  .build()
        };
        loanBookDao.saveAll(List.of(loans));

        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA - END --------");
    }
}
