At XING, people can create profiles. 
Each profile typically provides a CV (= current and previous work experience entries). 
The corresponding data is stored in the following table:

| ID  | JOB_TITLE       | START | END  |
|-----|-----------------|-------|------|
| 42  | J2EE developer  | 2017  | NULL |
| 42  | Web developer   | 2015  | 2016 |
| 76  | Data Scientinst | 2016  | NULL |
| 76  | Web developer   | 2013  | 2015 |
| 103 | J2EE Developer  | 2016  | NULL |
| 103 | Web developer   | 2011  | 2015 |

Please write a SQL query that returns the top 3 most prominent job titles that people work in after having worked as "Web developer". 
Hence, given all the users (identified by user_id) who worked as "Web developer" once, what are the top 3 job titles that most users have/had somewhen after ending their "Web developer" position? 
Please return both the job title and the number of users that work in that job.

| JOB_TITLE       | COUNT | 
|-----------------|-------|
| J2EE developer  |   2   | 
| Data Scientinst |   1   |

 
In the example above, there are 2 users who worked as "J2EE developer" and 1 user who worked as "Data Scientist" after working as "Web developer", i.e. the expected output would be:
 