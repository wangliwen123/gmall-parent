package com.atguigu.gmall.search.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    /**
     * "account_number": 25,
     *           "balance": 40540,
     *           "firstname": "Virginia",
     *           "lastname": "Ayala",
     *           "age": 39,
     *           "gender": "F",
     *           "address": "171 Putnam Avenue",
     *           "employer": "Filodyne",
     *           "email": "virginiaayala@filodyne.com",
     *           "city": "Nicholson",
     *           "state": "PA"
     */
    private Long account_number;
    private Long balance;
    private String firstname;
    private String lastname;
    private Integer age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;


}
