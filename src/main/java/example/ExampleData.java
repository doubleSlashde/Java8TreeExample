package example;

import java.math.BigDecimal;

public class ExampleData {
    String name;
    int num;
    BigDecimal foo;

    public ExampleData(String name, int num) {
        super();
        this.name = name;
        this.num = num;
    }
    
    public String getName() {
        return name;
    }
    
    public int getNum() {
        return num;
    }
    
    public BigDecimal getNumAsBigDecimal() {
        return BigDecimal.valueOf(num);
    }
    
    public BigDecimal getFoo() {
        return BigDecimal.valueOf(num * 10);
    }
    
}