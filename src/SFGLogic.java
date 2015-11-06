import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;


public class SFGLogic {
	StringBuilder sb;
	SFGLogic(int n, double[][] g, int s, int d){
		sb = new StringBuilder();
		nodes = n;
		this.g = g;
		this.s = s;
		this.d = d;
		v = new boolean[nodes];
		parent = new int[nodes];
	}
	

	void clearParentAndV(){
		for(int i=0; i<nodes; i++){
			parent[i] = i;
			v[i] = false;
		}
	}
	ArrayList<Integer> createPath(int s, int d){
		ArrayList<Integer> p = new ArrayList<Integer>();
		int i = d;
		while(i!=s){
			p.add(0,i);
			i=parent[i];
		}
		p.add(0,i);
		return p;
	}
	void savePath(int s, int d){
		if(fPaths==null)
			fPaths = new ArrayList<ArrayList<Integer>>();
		fPaths.add(createPath(s,d));
	}
	void saveLoop(int s, int d){
		ArrayList<Integer> p = new ArrayList<Integer>();
		p = createPath(s,d);
		p.add(s);
		if(loops==null)
			loops = new ArrayList<ArrayList<Integer>>();
		loops.add(p);
	}
	
	
	void getForwardPaths(){
		clearParentAndV();
		dfs(s);
	}
	void dfs(int i){
		if(i == d){
			savePath(s,d);
			return;
		}
		v[i] = true;
		for(int j=0; j<nodes; j++){
			if(g[i][j]!=0 && !v[j]){
				parent[j] = i;
				dfs(j);
				v[j]=false;
			}
		}
	}
	
	void getLoops(){
		for(int i=0; i<nodes; i++){
			clearParentAndV();
			stNode = i;
			dfs2(i);
		}
	}
	void dfs2(int i){
		v[i] = true;
		for(int j=stNode; j<nodes; j++){
			if(g[i][j]!=0 && !v[j]){
				parent[j] = i;
				dfs2(j);
				v[j]=false;
			} else if(g[i][j]!=0 && j==stNode)
				saveLoop(stNode, i);
		}
	}
	
	void showPathsAndLoops(){
		sb.append("\t\tForward Paths from "+s+" to "+d+"\n");
		sb.append("\t      ------------------------------\n");
		for(int i=0; i<fPaths.size(); i++){
			sb.append("N"+i+" "+fPaths.get(i).toString()+"\n");
//			sb.appenln(Integer.toBinaryString(getMask(f)));
		}
		sb.append("\t\t\tLoops\n");
		sb.append("\t\t      ----------\n");
		if(loops!=null)
			for(int i=0; i<loops.size(); i++){
				sb.append("L"+i+" "+loops.get(i).toString()+"\n");
	//			sb.appenln(Integer.toBinaryString(getMask(l)));
			}
		sb.append("\t------------------------------------------------------------\n");

	}
	void showDeltas(){
		for(int i=0; i<delta.length-1; i++){
			sb.append(delta[i]+"\n");
		}
	}
	
	double[][] g;
	boolean[] v;
	int[] parent;
	int nodes;
	int edges;
	int stNode;
	int s,d;
	ArrayList<ArrayList<Integer>> fPaths;
	ArrayList<ArrayList<Integer>> loops;
	////////////////////////////////////
	int getGain(ArrayList<Integer> p){
		int gain = 1;
		for(int i=0; i<p.size()-1; i++)
			gain*=g[p.get(i)][p.get(i+1)];
		return gain;
	}
	int getMask(ArrayList<Integer> p){
		int m = 0;
		for(int i:p)
			m|=(1<<i);
		return m;
	}
	int[] delta;
	String term(ArrayList<Integer> t){
		String s="";
		if(t.size()!=1)
			s="(";
		for(int i=0; i<t.size(); i++)
			s+= "L("+t.get(i)+")"+(i==t.size()-1?"":"*");
		if(t.size()!=1)
			s+=")";
		return s;
	}
	void showSOP(ArrayList<ArrayList<Integer>> SOP, int sign){
		if(!SOP.isEmpty())
			sb.append(sign==1?"+ ( ":"- ( ");
		for(ArrayList<Integer> p:SOP)
			sb.append(term(p)+(p==SOP.get(SOP.size()-1)?" )":" + "));
		sb.append("\n");
	}
	
	
	public void constructDeltas(){
		delta = new int[fPaths.size()+1];
		Arrays.fill(delta, 1);
		for(int i=0; i<delta.length; i++)		//last one is delta (without removing any path)
			constructDelta(i);
	}
	void assignIndividualLoops(ArrayList<ArrayList<Integer>> SOP, ArrayList<Integer> newLoops){
		for(int l:newLoops){
			ArrayList<Integer> i = new ArrayList<Integer>();
			i.add(l);
			SOP.add(i);
		}
		
	}
	public void constructDelta(int i){
		//remove loops touching path(i) to form Non Touching Loops
		ArrayList<Integer> newLoops = loopsWithoutPath(i);
		ArrayList<ArrayList<Integer>> SOP = new ArrayList<ArrayList<Integer>>();
		assignIndividualLoops(SOP, newLoops);
		//
		sb.append("Δ"+(i==delta.length-1?"":i) +  "=\n1\n");
		showSOP(SOP,-1);
		
		int sop = getSum(SOP);	//individual loops
		int sign = -1;
		while(sop!=0){
			delta[i]+=sign*sop;
			sign*=-1;
			SOP = getNextSOP(SOP, newLoops);
			showSOP(SOP,sign);
			sop = getSum(SOP);
			//
		}
		sb.append("value: "+delta[i]+"\n");
		sb.append("\t-------------\n");
	}
	ArrayList<Integer> loopsWithoutPath(int i){
		ArrayList<Integer> nLoops = new ArrayList<Integer>();
		int p = (i==delta.length-1)? 0:getMask(fPaths.get(i));
		if(loops!=null)
			for(int j=0; j<loops.size(); j++){
				int l = getMask(loops.get(j));
				if((l&~p)==l) // path doesn't touch loop
					nLoops.add(j);
			}
		return nLoops;
	}
	ArrayList<ArrayList<Integer>> getNextSOP(ArrayList<ArrayList<Integer>> SOP, ArrayList<Integer>newLoops){
		ArrayList<ArrayList<Integer>> ans = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> p:SOP){
			for(int j=p.get(p.size()-1)+1; j<newLoops.size(); j++){
				if(nonTouching(p, newLoops.get(j))){
					ArrayList<Integer> np = (ArrayList<Integer>) p.clone();
					np.add(newLoops.get(j));
					ans.add(np);
				}
			}
		}
		return ans;
	}
	boolean nonTouching(ArrayList<Integer> l, int i){ // l is list of loops(indices) , i (index) of loop
		int l1 = getMask(loops.get(i));
		for(int e:l){
			int l2 = getMask(loops.get(e));
			if((l1&~l2)!=l1)
				return false;
		}
		return true;
	}
	int getSum(ArrayList<ArrayList<Integer>> NTL){
		int sum = 0;
		for(ArrayList<Integer> L:NTL){
			int p = 1;
			for(int e:L)
				p*=getGain(loops.get(e));
			sum+=p;
		}
		return sum;
	}
	///////////////////////////////////
	void calcMasons(){
		sb.append("y"+d+"/"+"y"+s+" =\n(");
		
		double m = 0.0;
		for(int i=0; i<fPaths.size(); i++){
			sb.append(("M"+i+" * "+"Δ"+i )+ (i==fPaths.size()-1?"":" + "));
			m+=getGain(fPaths.get(i))*delta[i];
		}
		sb.append(" ) / Δ =\n");
		m/= delta[delta.length-1];
		
		sb.append(m+"\n");
	}
	StringBuilder excute(){
		getForwardPaths();
		getLoops();
		showPathsAndLoops();
		constructDeltas();	
		calcMasons();
		return sb;
	}
}
