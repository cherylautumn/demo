package stats.test;

import org.rosuda.JRI.Rengine;
import java.util.Enumeration;



import org.rosuda.JRI.REXP;
 
import org.rosuda.JRI.RList;
 
import org.rosuda.JRI.RVector;
 
import org.rosuda.JRI.Rengine;
 

public class DemoRcox {

    public static void main(String[] args) {
    	DemoRcox demo = new DemoRcox();
        demo.callRJava();
    }

    public void callRJava() {
    	Rengine re = new Rengine(new String[] { "--vanilla" }, false, null);
        System.out.println("Rengine created, waiting for R");
 
        // the engine creates R is a new thread, so we should wait until it's
        // ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        re.eval("library(survival)");
        re.eval("library(KMsurv)");
       
        re.eval("Rossi<-read.csv(\"/Users/cheryl/DevWorkSpace/javastat/DataSet/test10.csv\", header=T, sep=\",\") ");
        REXP coxfit=re.eval("coxfit<-coxph(Surv(Period,Censored)~D0+D1+D2+D3+D4+D5+D6+D7+D8+D9+D10+D11+D12+D13+D14+D15+D16, data=Rossi)");
       
       // String coxph="coxph(Surv(Period,Censored)~D0+D1+D2+D3+D4+D5+D6+D7+D8+D9+D10+D11+D12+D13+D14+D15+D16, data=Rossi)";
//        String deviance="devi<-resid("+coxph+",type='deviance')";
        String deviance="devi<-resid(coxfit,type='deviance')";
        
        System.out.println(deviance);
        re.eval(deviance);
        REXP devianceResi=re.eval("devi");        
        double[] devianceList=devianceResi.asDoubleArray();
        System.out.println("deviance size: "+devianceList.length);
        
       
        REXP hazardResi=re.eval("coxfit$linear.predictors");
        
        double[] hazardList =hazardResi.asDoubleArray();
        
     
        System.out.println("hazard size: "+hazardList.length);
//        for (int i=0;i<devianceList.length;i++){
//        	System.out.print(devianceList[i]+"/");
//        }
        re.end();
        
        
        
        REXP t=re.eval("R.home(component=\"home\")");
        
        re.eval("data(iris)",true);
        REXP x = re.eval("iris");
        // generic vectors are RVector to accomodate names
        RVector v = x.asVector();
        System.out.println("has names:");
/*        for (Enumeration e = v.getNames().elements(); e.hasMoreElements();) {
            System.out.println(e.nextElement());
        }
 
        // for compatibility with Rserve we allow casting of vectors to lists
        RList vl = x.asList();
        String[] k = vl.keys();
        System.out.println("and once again from the list:");
        int i = 0;
        while (i < k.length)
            System.out.println(k[i++]);
 
        // get boolean array
        System.out.println(x = re.eval("iris[[1]]>mean(iris[[1]])"));
 
        // R knows about TRUE/FALSE/NA, so we cannot use boolean[] this way
        // instead, we use int[] which is more convenient (and what R uses
        // internally anyway)
        int[] bi = x.asIntArray();
        for (int j : bi) {
            System.out.print(j == 0 ? "F " : (j == 1 ? "T " : "NA "));
        }
        System.out.println("");
 
        // push a boolean array
        boolean by[] = { true, false, false };
        re.assign("bool", by);
        System.out.println(x = re.eval("bool"));
 
        // asBool returns the first element of the array as RBool
        // (mostly useful for boolean arrays of the length 1). is should
        // return true
        System.out.println("isTRUE? " + x.asBool().isTRUE());
 
        // now for a real dotted-pair list:
        System.out.println(x = re.eval("pairlist(a=1,b='foo',c=1:5)"));
        RList l = x.asList();
 
        int idx = 0;
        String[] a = l.keys();
        System.out.println("Keys:");
        while (idx < a.length)
            System.out.println(a[idx++]);
 
        System.out.println("Contents:");
        idx = 0;
        while (idx < a.length)
            System.out.println(l.at(idx++));
 
        System.out.println(re.eval("sqrt(36)")); */
    }
}