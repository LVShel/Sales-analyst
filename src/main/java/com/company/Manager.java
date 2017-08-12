package com.company;


import com.company.utils.Gender;
import com.company.utils.ItemRank;

import java.time.LocalDate;

/**
 * This class represents such an entity as manager of product store.
 * It has only main method, which allows manager to call methods of all
 * classes in current package. However, due to business logic, all he needs
 * is to call methods of classes SalesDepartment, DBAdministrator using their instances.
 * @author  Leonid Shelest
 */
public class Manager {
    public static void main(String[] args) {
        SalesDepartment salesDepartment = new SalesDepartment();
        DBAdministrator dbAdministrator = new DBAdministrator();

        salesDepartment.initCustomers();
        salesDepartment.initItems();
        salesDepartment.refreshStoreTables();
        dbAdministrator.markPopularItemsInDB(5);
        dbAdministrator.markUnpopularItemsInDB(5);
        dbAdministrator.writePopularItemsToFile(5);
        dbAdministrator.writeUnpopularItemsToFile(5);

        salesDepartment.showItems(dbAdministrator.getItemsByRank(3, ItemRank.UNPOPULAR));
        salesDepartment.showItems(dbAdministrator.getPopularItemsByCustomersGender(Gender.MALE, 3));
        salesDepartment.showItems(dbAdministrator.getPopularItemsOfPeriod(LocalDate.of(2017, 6, 20), LocalDate.of(2017,7,21), 3));







    }

}
