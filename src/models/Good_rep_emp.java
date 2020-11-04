package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/*
@Table(name = "good_rep_emp")
@NamedQueries({
    @NamedQuery(
            name = "getGoodChk",
            query = "SELECT g FROM Good_rep_emp AS g where g.employee = :employee"
            )
*/

@Table(name = "good_rep_emp")

//ログインしている人専用
@NamedQueries({
    @NamedQuery(
            name = "getGoodChk",
            query = "SELECT g FROM Good_rep_emp AS g where g.employee = :employee"
            ),

    @NamedQuery(
            name = "getGoodFlg",
            query = "SELECT g FROM Good_rep_emp AS g where g.report= :report and  g.employee = :employee"
            ),

    @NamedQuery(
            name = "getGoodRepFlg",
            query = "SELECT g FROM Good_rep_emp AS g where g.report= :report"
            ),


//他の人のも見る用
    @NamedQuery(
            name = "getGoodChkAllemp",
            query = "SELECT g FROM Good_rep_emp g"
            )




  /*
    @NamedQuery(
            name = "getReportGoodchk",
            query = "select count(r) FROM Report As r where r.employee = :employee and r.id = :id"
            )
*/
})
@Entity
public class Good_rep_emp {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    //ログインしている従業員の情報をオブジェクトのまま employee フィールドに格納します
    //これ何に使っている？employee_idの設定？？どうやってidが導き出される？
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "good_flg", nullable = false)
    private Integer good_flg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Integer getGood_flg() {
        return good_flg;
    }

    public void setGood_flg(Integer good_flg) {
        this.good_flg = good_flg;
    }

}

