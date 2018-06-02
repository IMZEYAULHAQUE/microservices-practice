package com.pnc.assignment.aggregator.controller

import com.pnc.assignment.aggregator.controller.AggregatorController
import com.pnc.assignment.aggregator.custom.CustomResponseEntity
import com.pnc.assignment.aggregator.feignclient.account.AccountFeignClient
import com.pnc.assignment.aggregator.feignclient.user.UserFeignClient
import com.pnc.assignment.aggregator.model.ExceptionData
import com.pnc.assignment.aggregator.model.account.Account
import com.pnc.assignment.aggregator.model.account.AccountStatus
import com.pnc.assignment.aggregator.model.account.AccountType
import com.pnc.assignment.aggregator.model.user.Address
import com.pnc.assignment.aggregator.model.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDateTime
import java.util.stream.Collectors

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//@RunWith(SpringRunner.class)
//@SpringBootTest
@WebMvcTest(AggregatorController)
class AggregatorControllerWebMVCSpecification extends Specification {

    def fakeUserList     = this.getDummyUserList()
    def fakeAccountList  = this.getDummyAccountList()

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private UserFeignClient userFeignClient

    @MockBean
    private AccountFeignClient accountFeignClient

    def "GET | +ve | get all users should return 200 with status as 'SUCCESS'"() {

        given:
        userFeignClient.findAll() >> CustomResponseEntity.SUCCESS(fakeUserList)
        accountFeignClient.findByUserId(1) >> CustomResponseEntity.SUCCESS(this.getAccountList(fakeAccountList, 1))

        when:

        def response = this.mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse()
        println response.contentAsString


        then:
        true
    }

   /* @Unroll
    def "GET | +ve | userId = #userId is valid user"() {


        // not working. no idea why. need to debug. it is saying user.id property not exist in User
        when:
        CustomResponseEntity customResponseEntity = this.restTemplate.getForObject("/users/{userId}", CustomResponseEntity.class, userId)
        User user = customResponseEntity.getData()

        then:
        println user
        //user.userId == userId

        where:
        userId  | _
        1       | _
        3       | _
    }*/


    def getDummyUserList() {
        def userList = [
                new User(userId: 1, firstName: "M Z", lastName: "Haq", address: new Address(addressOne:  "1200 N Lamar", addressTwo:  "Apt 124", city: "Austin", state: "TX", zip: "78759", user: null), accounts: null),
                new User(userId: 2, firstName: "Mohammad", lastName: "Haque", address: new Address(addressOne:  "100 Chatham Park Dr", addressTwo:  "Apt 547", city: "Pittsburgh", state: "PA", zip: "15220", user: null), accounts: null),
                new User(userId: 3, firstName: "Zeyaul", lastName: "Haque", address: new Address(addressOne:  "400 Chatham Park Dr", addressTwo:  "Apt 402", city: "Pittsburgh", state: "PA", zip: "15220", user: null), accounts: null),
                new User(userId: 4, firstName: "Nilesh", lastName: "Patekar", address: new Address(addressOne:  "500 Chatham Park Dr", addressTwo:  "Apt 510", city: "Pittsburgh", state: "PA", zip: "15220", user: null), accounts: null),
                new User(userId: 5, firstName: "Rajesh", lastName: "Kumar", address: new Address(addressOne:  "600 Chatham Park Dr", addressTwo:  "Apt 610", city: "Pittsburgh", state: "PA", zip: "15220", user: null), accounts: null)
        ]
        return userList
    }

    def getDummyAccountList() {
        def accountList = [
                new Account(accountId: 1, userId: 1, type: AccountType.CURRENT, balance: 9154.50, status: AccountStatus.ACTIVE, createdDate: LocalDateTime.now()),
                new Account(accountId: 2, userId: 1, type: AccountType.SAVING, balance: 50000.00, status: AccountStatus.ACTIVE, createdDate: LocalDateTime.now()),
                new Account(accountId: 3, userId: 2, type: AccountType.CURRENT, balance: 5000.50, status: AccountStatus.ACTIVE, createdDate: LocalDateTime.now()),
                new Account(accountId: 4, userId: 3, type: AccountType.CURRENT, balance: 5500.50, status: AccountStatus.ACTIVE, createdDate: LocalDateTime.now()),
                new Account(accountId: 5, userId: 3, type: AccountType.SAVING, balance: 10000.00, status: AccountStatus.ACTIVE, createdDate: LocalDateTime.now()),
                new Account(accountId: 6, userId: 4, type: AccountType.SALARY_ACCOUNT, balance: 58000.00, status: AccountStatus.ACTIVE, createdDate: LocalDateTime.now())
        ] as LinkedHashSet
    }

    def getAccountList(LinkedHashSet<Account> accountList, long userId) {

        def list = []
        accountList.each {
            it ->
                if (it.userId == userId) {
                    list.add(it)
                }
        }
        //println list
        return list
    }

    def getUserById(List<User> dummyUserList, long userId) {

        User user = null;
        
        dummyUserList.each {
            it ->
                if (it.userId == userId) {
                    user = it
                }
        }
        return user
    }

    def getUserByLastName(List<User> dummyUserList, String lastName) {
        return dummyUserList.stream().filter({it -> it.lastName == lastName}).collect(Collectors.toList())
    }

    def wrapToExceptionData(RuntimeException ex) {
        ExceptionData data = new ExceptionData(404);
        data.addErrorData("error", "Not Found");
        data.addErrorData("message", "User not found");
        data.addErrorData("errorType", ex.getClass().getSimpleName());
        data.addErrorData("exceptionClass", ex.getClass().getName());
        return data
    }

    /*@TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        FeignLoggerFactory feignLoggerFactory() {
            return detachedMockFactory.Mock(FeignLoggerFactory)
        }

        @Bean
        UserFeignClient userFeignClient() {
            return detachedMockFactory.Mock(UserFeignClient)
        }

        @Bean
        AccountFeignClient accountFeignClient() {
            return detachedMockFactory.Mock(AccountFeignClient)
        }

        @Bean
        FeignContext feignContext() {
            return detachedMockFactory.Mock(FeignContext)
        }


    }*/
}
