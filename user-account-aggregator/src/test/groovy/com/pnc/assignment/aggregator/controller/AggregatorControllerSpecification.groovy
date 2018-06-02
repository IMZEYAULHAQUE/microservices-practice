package com.pnc.assignment.aggregator.controller

import com.pnc.assignment.aggregator.controller.AggregatorController
import com.pnc.assignment.aggregator.custom.CustomResponseEntity
import com.pnc.assignment.aggregator.exception.AccountNotFoundException
import com.pnc.assignment.aggregator.exception.InvalidAmountWithdrawException
import com.pnc.assignment.aggregator.exception.UserNotFoundException
import com.pnc.assignment.aggregator.feignclient.account.AccountFeignClient
import com.pnc.assignment.aggregator.feignclient.user.UserFeignClient
import com.pnc.assignment.aggregator.model.ExceptionData
import com.pnc.assignment.aggregator.model.account.Account
import com.pnc.assignment.aggregator.model.account.AccountStatus
import com.pnc.assignment.aggregator.model.account.AccountType
import com.pnc.assignment.aggregator.model.user.Address
import com.pnc.assignment.aggregator.model.user.User
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.util.stream.Collectors

class AggregatorControllerSpecification extends Specification {

    def userClient       = Stub(UserFeignClient)
    def accountClient    = Stub(AccountFeignClient)
    def controller       = new AggregatorController(userClient: userClient, accountClient: accountClient)
    def fakeUserList     = this.getDummyUserList()
    def fakeAccountList  = this.getDummyAccountList()

    def "GET | +ve | get all users should return 200 with status as 'SUCCESS'"() {

        given:
        userClient.findAll() >> CustomResponseEntity.SUCCESS(fakeUserList)
        accountClient.findByUserId(userId) >> CustomResponseEntity.SUCCESS(this.getAccountList(fakeAccountList, userId))

        when:
        List<User> userList = controller.findAllUsers()
        //println userList

        then:
        userList !=  null
        userList[0].userId == userId

        where:
        userId  | _
        1       | _
    }

    @Unroll
    def "GET | +ve | userId = #userId is valid user"() {

        given:
        User user = this.getUserById(fakeUserList, userId)
        userClient.findUser(userId) >> (user == null ? CustomResponseEntity.FAILURE(this.wrapToExceptionData(new UserNotFoundException("User not found"))) : CustomResponseEntity.SUCCESS(user))
        accountClient.findByUserId(userId) >> CustomResponseEntity.SUCCESS(this.getAccountList(fakeAccountList, userId))

        when:
        User foundUser = controller.findUser(userId)

        then:
        foundUser == user

        where:
        userId  | _
        1       | _
        3       | _
    }

    @Unroll
    def "GET | -ve | userId = #userId doesn't exist"() {

        given:
        //userClient.findUser(userId) >> CustomResponseEntity.FAILURE(this.wrapToExceptionData(new UserNotFoundException("User not found")))
        userClient.findUser(userId)  >> {throw new UserNotFoundException("Invalid User")}
        accountClient.findByUserId(userId) >> CustomResponseEntity.SUCCESS(this.getAccountList(fakeAccountList, userId))

        when:
        controller.findUser(userId)

        then:
        thrown(UserNotFoundException)

        where:
        userId  | _
        9999    | _
    }

    @Unroll
    def "GET | -ve | userId = #userId exist but without account"() {

        setup:
        User user = this.getUserById(fakeUserList, userId)
        userClient.findUser(userId) >> (user == null ? CustomResponseEntity.FAILURE(this.wrapToExceptionData(new UserNotFoundException("User not found"))) : CustomResponseEntity.SUCCESS(user))
        accountClient.findByUserId(userId) >> {throw new AccountNotFoundException("Account not found")}

        when:
        controller.findUser(userId)

        then:
        thrown(AccountNotFoundException)

        where:
        userId   | _
        1        | _
    }

    @Unroll
    def "GET | -ve | userId = #userId out of long range"() {

        when:
        controller.findUser(userId)

        then:
        thrown(MissingMethodException)

        where:
        userId  | _
        4655698546546546546546541265| _
    }

    @Unroll
    def "GET | +ve | User with Last Name = #userLastName exist : #userExist"() {

        given:
        List<User> users = this.getUserByLastName(dummyUserList, userLastName)
        userClient.findUserByLastName(userLastName) >> CustomResponseEntity.SUCCESS(users)
        accountClient.findByUserId(_) >> CustomResponseEntity.SUCCESS(this.getAccountList(fakeAccountList, 1))

        when:
        List<User> userList = controller.findUserByLastName(userLastName)

        then:
        userList.size() == users.size()
        userList.size() == 0 || userList.each {it -> it.lastName == userLastName}

        where:
        userLastName  | userExist
        "Haque" | "true"
        "Bond" | "false"
    }

    @Unroll
    //@IgnoreRest
    def "PUT | +ve | updating user for user id = #userId"() {

        given:
        userClient.update(user, userId) >> CustomResponseEntity.SUCCESS("User updated successfully.")

        when:
        def response = controller.updateUser(user, userId)

        then:
        response == "User updated successfully."

        where:
        userId  | user
        1       | new User('M Zeyaul', 'HAQUE', new Address(addressOne: 'Invalid Address One', addressTwo: 'Apt 000', city: 'Pittsburgh', state: 'PA', zip: '15106', user: null))
    }

    @Unroll
    def "DELETE | +ve | Delete user for userId = #userId"() {

        userClient.delete(userId) >> CustomResponseEntity.SUCCESS("User deleted successfully.")

        when:
        def response = controller.deleteUser(userId)

        then:
        response == "User deleted successfully."

        where:
        userId  |   _
        1       | _
    }

    @Unroll
    def "DELETE | -ve | Delete user for userId = #userId not exist"() {

        given:
        userClient.delete(userId) >> {throw new UserNotFoundException("User not found.")}

        when:
        controller.deleteUser(userId)

        then:
        thrown UserNotFoundException

        where:
        userId  |   _
        -7       | _
    }

    @Unroll
    def "DELETE | +ve | close user account for userId = #userId and accountId = #accountId"() {

        setup:
        accountClient.close(userId, accountId) >> CustomResponseEntity.SUCCESS("Account closed successfully.")

        when:
        def response = controller.closeAccount(userId, accountId)

        then:
        response == "Account closed successfully."

        where:
        userId  |   accountId
        3       |   5
    }

    @Unroll
    def "GET | +ve | get balance for userId = #userId and accountId = #accountId"() {

        given:
        accountClient.getBalance(userId, accountId) >> CustomResponseEntity.SUCCESS(4500.50 as Double)

        when:
        def response = controller.getBalance(userId, accountId)

        then:
        response == 4500.50

        where:
        userId  |   accountId
        3       |   5
    }

    @Unroll
    def "PUT | +ve | withdraw #amount dollar from accountId = #accountId"() {

        given:
        String[][] data = [["newBalance", "5000.50"]]
        accountClient.withraw(["withdrawAmount":amount], userId, accountId) >> CustomResponseEntity.SUCCESS(data)

        when:
        def response = controller.withraw(["withdrawAmount":amount], userId, accountId)

        then:
        Double.valueOf(response[0][1]) >= 0

        where:
        amount  |   userId  |   accountId
        10      |   3       |   5
    }

    @Unroll
    def "PUT | -ve | withdrawing Invalid amount (#amount) should throw exception"() {

        accountClient.withraw(["withdrawAmount":amount], userId, accountId) >> {throw new InvalidAmountWithdrawException("Invalid amount exception")}
        when:
        controller.withraw(["withdrawAmount":amount], userId, accountId)

        then:
        thrown(InvalidAmountWithdrawException)

        where:
        amount   |   userId  |   accountId
        -5       |   5       |   7
        99999999 |   5       |   7
    }

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
}
