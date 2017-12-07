/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Model.*;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Gleb
 */
public interface IQueries {
    //1. Cписок студентов, проходивших тест А, по алфавиту
    List<User> getListOfStudentsPassedSpecificTest(UUID testId);
    //1. Список тестов, написанных преподавателем А, по алфавиту
    List<Test> getListOfTestsBySpecificTeacher(UUID teacherId);
    
    //2. Поиск студента по ключу
    User getStudentByKey(String key);    
    //2. Поиск теста по ключу
    Test getTestByKey(String key);
    
    //3. Список студентов со ср. баллом между А и Б
    List<User> getListOfStudentsWithAvgMarkInRange(double from, double to);
    //3. Список студентов с максимальным баллом между А и Б
    List<User> getListOfStudentsWithMaxMarkInRange(double from, double to);
    
    //4. Сколько студентов имеют оценки ниже A?
    int countStudentsWhoHasMarksUnder(double markValue);
    //4. Сколько студентов имеют оценки выше А?
    int countStudentsWhoHasMarksAbove(double markValue);
    
    //5. Сколько тестов сдал каждый студент?
     List<List<Object>> forEachStudentCountTestsHePasses();
    //5. Сколько тестов сделал каждый преподаватель?
     List<List<Object>> forEachTeacherCountTestsHeMade();
    
    //6 Кто из студентов сдал больше всех тестов?
    User getStudentWhoPassedMostTests();
    //7 Кто из преподавателей сделал больше всех тестов?
    User getTeacherWhoMadeMostTests();
    
    //7. Для каждого теста определить студента, который сдал его лучше всего. 
    List<List<Object>> forEachTestGetStudentWhoPassedItBest();
    //7. Для каждого преподавателя определит студента с лучшим средним баллом.  
    List<List<Object>> forEachTeacherGetStudentWithHighestAvgMark();
    
    //8. Кто из студентов не сдал ни одного теста?
    List<User> getListOfStudentsWhoPassedNoTests();
    //8. Кто из преподавателей не сделал ни одного теста?
    List<User> getListOfTeachersWhoMadeNoTests();
    
    //9. Список студентов с комментарием "имеет тесты сданные на отлично", "не имеет тестов сданных на отлично"
    List<User> getListOfStudentsWithCommentWhetherTheyHaveAnyTestsWithHighestMarkOrNot();
    //9. Список студентов с комментариями "имеет сданные ниже проходного балла", "не имеет тестов сданных ниже рпоходного балла"
    List<User> getListOfStudentsWithCommentWhetherTheyHaveAnyTestsWithLowestMarkOrNot();
    
    //10. Поле дополнительыне свдедения у студента, например "сдал наибольшее количество тестов"
    User getStudentWithAdditionalData();
    //10. Поле дополнительыне свдедения у преподавателя например "сделал наибольшее количество тестов"
    User getTeacherWithAdditionalData();
}

/*1.	Вибір з декількох таблиць із сортуванням (наприклад, список пацієнтів, яких лікує заданий лікар);
2.	Завдання умови відбору з використанням предиката LІKE (наприклад, знайти всіх пациєнтів, чиє прізвище починається на задану букву);
3.	Завдання умови відбору з використанням предиката BETEWEEN (наприклад, список пацієнтів, прооперованих у заданий період);
4.	Агрегатна функція без угруповання (наприклад, скільки пацієнтів надійшло в лікарню за останній тиждень?);
5.	Агрегатна функція з угрупованням (наприклад, скільки пацієнтів прооперував кожний лікар?);
6.	Використання предиката ALL або  ANY (наприклад, хто з лікарів прооперировал найбільшу кількість хворих?);
7.	Корельований підзапит (наприклад, для кожної лікарської  спеціальності визначити лікаря, що лікує максимальну кількість пацієнтів);
8.	Запит на заперечення (наприклад, хто з лікарів не оперував на цьому тижні?) Запит реалізувати у трьох варіантах: з використанням  LEFT JOІN, 
предиката ІN і предиката EXІSTS;
9.	Операція об'єднання UNІON із включенням коментарю в кожен рядок (наприклад, список лікарів з коментарем «Має максимальну кількість хворих»,
«Не має в цей час хворих»);
10.	Відновлення даних, умова відбору формується з використанням підзапита з іншої таблиці
(наприклад, у поле ДодатковіВідомості для відповідного лікаря записати «Прооперував найбільшу кількість хворих»).

Отчеты по 5,7,9 запросу
*/