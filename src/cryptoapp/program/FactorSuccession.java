package cryptoapp.program;

import co.edu.unal.crypto.tools.PollardsRho;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import co.edu.unal.system.Program;
import co.edu.unal.system.TextConsole;
import java.awt.Component;
import java.math.BigInteger;
import javax.swing.JOptionPane;

/**
 *
 * @author eduarc
 */
public class FactorSuccession extends Program {

    public static final String CMD_FACTOR_SUCCESSION = "fac_succession";
    public static final String P_N = "n";

    TextConsole stdout;
    
    public FactorSuccession(Environment env) {
        super(env);
        
        stdout = (TextConsole) env.getResource(Environment.STDOUT);
    }
    
    @Override
    public int exec(Param[] params) {
        
        if (!ParamUtils.contains(params, P_N)) {
            stdout.error("Parameter "+P_N+" not provided");
            return -1;
        }
        Integer n = 0;
        for (Param p : params) {
            if (p.getName().equals(P_N)) {
                n = getInt(p);
                if (n == null) {
                    return -1;
                }
            }
        }
        stdout.info("Input:");
        stdout.append("Succession: a(x) = [2^(x-1)]*[(2^x)+1]-(3^x)");
        stdout.info("Parameters:");
        stdout.append(P_N+" = "+n);
        stdout.info("Computing...");
        stdout.append("[x, a(x), non trivial factor of a(x)]");
        PollardsRho rho = new PollardsRho();
        BigInteger two = BigInteger.valueOf(2);
        BigInteger three = BigInteger.valueOf(3);
            // a(n) = [2^(n-1)]*[(2^n)+1]-(3^n)
        for (int i = 3; i <= n; i++) {
            if (i == 422) {
                continue;
            }
            BigInteger x = two.pow(i-1);
            x = x.multiply(two.pow(i).add(BigInteger.ONE));
            x = x.subtract(three.pow(i));
            stdout.append("["+i+", "+x+", "+rho.nonTrivialFactor(x)+']');
        }
        return 0;
    }

    public Integer getInt(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog((Component) this.getEnv().getResource(Environment.FRAME), "Parameter "+p.getName());
        }
        if (strN == null) {
            return null;
        }
        try {
            return Integer.parseInt(strN);
        } catch(NumberFormatException ex) {
            stdout.error("Invalid value for parameter "+p.getName()+". "+ex.getMessage());
        }
        return null;
    }
    
    @Override
    public String getName() {
        return CMD_FACTOR_SUCCESSION;
    }
    
}
