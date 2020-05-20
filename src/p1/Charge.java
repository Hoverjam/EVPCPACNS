package p1;
import java.awt.Checkbox;
import java.util.Random;
import java.util.Scanner;
public class Charge {
	final static int M = 100;  //车的数量
	final static int C = 7;  //充电站数量
	final static double P = 30;  //充电功率
	final static int D = 36;  //所有节点数
	final static double u = 1;
	final static double v = 1;  //车速度
	final static double delta = 0.1;
	final static double lam[] = { 0.3,0.45,0.2,0.56,0.43,0.39,0.55 };
	final static int INF = 999999;
	final static int L = 1296;
	final static double N=1;     //SOC转化为里程数的能量等效系数
	final static int cbar[] = { 7,5,6,5,10,4,6 };  //每个充电站的充电桩数量
	final static double FULL =300000;
	static double SOC[] = { 0.30802,0.40934,0.26668,0.57,0.42338,0.35448,0.26956,0.62716,0.57924,0.52928,0.1541,0.6029,0.50562,0.37654,0.23922,0.24982,0.1999,0.27884,0.23654,0.14872,0.68782,0.33208,0.11804,0.42106,0.44184,0.28764,0.38842,0.41432,0.43436,0.4379,0.14894,0.47452,0.33542,0.27076,0.61738,0.43824,0.55334,0.56598,0.3807,0.23788,0.61406,0.51622,0.66644,0.64666,0.39346,0.13328,0.34282,0.19422,0.60506,0.17736,0.55094,0.59288,0.69324,0.69514,0.44074,0.29718,0.21446,0.23482,0.59058,0.05556,0.28632,0.1007,0.4838,0.07684,0.42576,0.64212,0.2208,0.21884,0.42528,0.49296,0.58892,0.5161,0.3578,0.17458,0.5274,0.347,0.34012,0.66202,0.52786,0.11096,0.43258,0.29246,0.52168,0.43908,0.41512,0.2768,0.13932,0.18752,0.31862,0.56616,0.37888,0.68878,0.53252,0.26646,0.15074,0.47076,0.36236,0.08164,0.49858,0.37082, };//电动汽车剩余电量
	static int reachablestation[][] = new int[M][C];
	static int []zuijin = new int[M];
	static int reachablestationnum[] = new int[M];
	static int yongdu_edge[][] = new int[D][D];

	static final double aEdge [][] = {
			{0,  INF,  INF , INF, INF, INF, INF, INF, 2246.2, INF, INF, INF, 2178.1, 1832.7,  INF, INF, INF, 2144.2, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF , INF, INF, INF, INF, INF, INF,},
            {INF, 0, INF, INF, INF, INF, INF, INF, INF,2064.7,1764.1,INF, INF, INF, 2204.7, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,},
            {INF, INF,  0, INF, INF, INF, INF, INF, INF, INF,2258.4,INF, INF, INF, 1946.6,  1887.6, INF, INF, INF, INF,2212.6,  INF, INF,INF, INF, INF, INF, INF, INF, INF,INF,  INF, INF, INF,  INF,  INF,},
            {INF,  INF,  INF,  0, INF, INF, INF,INF,  INF, INF,INF,INF, INF,INF, INF,INF, INF,INF, 2115.5, INF,INF, INF,INF, 2165,  1744.1, INF, INF, INF, 2160.7, INF,INF, INF, INF, INF, INF, INF,},
            {INF, INF,  INF,INF,  0, INF, INF, INF, INF, INF,INF, INF, INF, INF, INF,INF,INF, INF, INF,INF,INF, INF,  INF, INF, INF, INF,  2079.1, INF,  INF, INF,INF, INF,  2184.8, INF, INF, INF,},
            {INF,  INF, INF, INF, INF,  0,INF, INF, INF, INF,INF, INF, INF, INF, INF, INF,INF, INF, INF, INF,INF,  1811.6, INF,  INF, INF,  2374.9, INF,  INF, INF, INF,INF,  1950.8, INF, INF, INF, INF,},
            {INF, INF,INF,  INF, INF,  INF,  0, INF, INF, INF,INF, INF,INF, INF, INF, INF, INF, INF, INF,  INF,INF, INF, INF, INF,INF, INF, INF, INF, INF, INF,2031.5, INF, INF, INF,  1870.2,  2313.4,},
            {INF,  INF, INF,  INF,INF, INF, INF,  0,  2036.9, INF,INF,INF,  2238.1,INF,INF, INF, INF, INF, INF, INF,INF, INF,INF, INF, INF, INF, INF, INF, INF, INF,INF, INF, INF, INF, INF,  INF,},
            {2268.2, INF, INF, INF, INF, INF,INF, 2122.1,  0,  2265.4,INF, INF, INF, INF, INF, INF, INF,INF, INF, INF, INF, INF, INF,INF, INF, INF, INF, INF, INF, INF,INF, INF, INF, INF, INF, INF,},
            {INF,  2369.5, INF, INF, INF,  INF,  INF, INF,  2295.5,  0,INF, INF, INF, 2283.1, INF, INF, INF,INF, INF, INF,INF, INF,INF,INF, INF, INF, INF, INF, INF,INF,INF,INF,INF,INF, INF,INF,},
            {INF,  2095,  2167.3, INF, INF, INF, INF, INF, INF, INF,0, 2001,INF, INF, INF, INF,INF, INF,INF,INF,INF, INF, INF,INF, INF, INF,INF, INF,INF, INF,INF, INF, INF, INF, INF, INF,},
            {INF, INF, INF, INF, INF, INF,INF, INF, INF,  INF, 2160.2,  0, INF, INF,INF,  2259.9, INF, INF, INF,INF,INF,INF, INF,INF, INF,INF,INF, INF, INF, INF,INF,INF, INF, INF, INF, INF,},
            {1968.7, INF,INF,  INF, INF, INF,INF,  2133.3,INF,INF,INF, INF,  0, INF, INF,INF, 2119.5,INF, INF, INF,INF, INF,INF, INF, INF,INF, INF,INF, INF, INF,INF,INF, INF, INF, INF,INF,},
            {2156, INF, INF,INF,INF, INF, INF, INF, INF,  2125.8,INF, INF, INF,  0,  2006.5, INF, INF, INF, 2112.5, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,},
            {INF,  2082.6,  1951.1, INF,INF,  INF, INF, INF,INF, INF,INF,INF,INF,1821.3,  0,INF, INF, INF, INF,  1807.8,INF,INF, INF,INF, INF, INF, INF, INF,INF, INF,INF,INF,INF, INF, INF, INF,},
            {INF,  INF,  2349.3,  INF, INF, INF,INF, INF, INF, INF,INF,  2085,INF, INF,INF,  0, INF, INF, INF, INF,INF,  2377.1, INF, INF, INF,  INF, INF, INF,INF, INF,INF, INF,INF, INF, INF, INF,},
            {INF, INF,  INF,INF, INF, INF, INF, INF, INF,INF,INF,INF,  2303.2, INF, INF,INF,  0,  2213.5,INF,INF,INF,INF,  2272.3, INF, INF, INF,INF, INF, INF, INF,INF,INF, INF, INF,INF, INF,},
            {2435, INF, INF,INF, INF, INF, INF, INF,INF, INF,INF, INF, INF, INF, INF, INF,2035.5,  0,  2035.5, INF,INF, INF,INF,  2137.7,INF, INF, INF,INF, INF, INF,INF,INF, INF, INF, INF, INF,},
            {INF, INF, INF, 2119.5,INF, INF,INF, INF, INF, INF,INF,INF, INF, 1964.5, INF, INF, INF,  1724,  0,  2175.8,INF, INF, INF, INF, INF, INF, INF, INF,INF, INF,INF, INF, INF, INF,INF, INF,},
            {INF, INF, INF, INF,INF, INF, INF, INF,INF,INF,INF,INF, INF, INF, 2389.2,INF, INF,INF,  1852.7,  0, 2057.5, INF,INF,  INF, 2024,  INF,INF, INF, INF,INF,INF, INF,INF, INF, INF,INF},
            {INF,  INF,  1811.9,INF, INF, INF, INF, INF,INF, INF,INF, INF,INF, INF, INF, INF, INF, INF, INF,  1836.4 ,0 , 2157.5, INF, INF, INF, 1917.8, INF, INF, INF, INF,INF, INF, INF, INF,INF, INF,},
            {INF, INF, INF, INF, INF, 2097.2,INF, INF, INF, INF,INF, INF, INF,INF, INF,  1746.9,INF, INF,INF, INF,2313.7,  0, INF, INF, INF,INF, INF, INF, INF, INF,INF,INF, INF, INF, INF, INF,},
            {INF, INF,INF, INF,INF,INF,INF, INF, INF,INF,INF,INF, INF,INF,INF, INF,  2172.4,INF, INF, INF,INF,INF,  0,  1945.5,INF, INF,  2291.1,INF, INF,INF,INF,INF, INF, INF, INF, INF,},
            {INF, INF, INF, 2169.6, INF, INF, INF, INF,INF, INF,INF,  INF,INF,  INF, INF, INF,  INF,  1853.6, INF, INF,INF, INF, 2133.8,  0, INF,  INF,  INF,  2185.5,  INF,  INF,INF, INF, INF,INF, INF, INF,},
            {INF, INF,INF, 2395.6, INF, INF,INF,  INF,  INF, INF,INF, INF, INF, INF, INF,INF, INF,  INF, INF,  2471.3,INF, INF, INF,INF,  0,  1735.8,INF, INF, INF,  1976.4,INF,INF,  INF, INF,INF, INF,},
            {INF, INF, INF, INF, INF,  2272, INF, INF,INF,  INF,INF,INF, INF, INF, INF, INF, INF, INF, INF, INF,1856.9, INF, INF, INF,  2322.2,  0, INF,  INF,INF, INF, 2200.8,INF, INF,INF, INF, INF,},
            {INF,INF, INF, INF,  2447.1, INF, INF, INF, INF, INF,INF,INF, INF,  INF, INF, INF,  INF, INF, INF, INF,INF, INF,  2145.1, INF,INF, INF,  0,  2139.8,INF, INF,INF,INF, INF, INF, INF, INF,},
            {INF,INF, INF, INF, INF,INF,  INF, INF,INF,  INF,INF, INF, INF, INF, INF, INF, INF, INF,  INF,  INF,INF, INF,INF,  2219.3, INF, INF,  2061.6,  0,  2116.7,INF,INF, INF,  1992.1, INF, INF, INF,},
            {INF,INF, INF,  1958.7, INF,  INF, INF, INF, INF, INF,INF, INF, INF, INF, INF, INF, INF, INF, INF,  INF,INF,  INF,  INF,INF,  INF,INF,  INF, 2716, 0, 2151.2,INF, INF, INF, 1730.9, INF,  INF,},
            {INF,  INF,  INF, INF,  INF, INF, INF,INF,INF, INF,INF,INF,  INF, INF, INF, INF, INF, INF,INF, INF,INF, INF,  INF, INF,  1815.9, INF, INF,  INF,  1796.2,  0,2265.8, INF, INF, INF,  2353.7,  INF,},
            {INF, INF, INF, INF,  INF,  INF,  1979.2,INF, INF, INF,INF, INF,  INF,  INF,INF,  INF, INF,  INF, INF, INF,INF, INF, INF, INF, INF,3216.6,INF, INF, INF, 2006,0,  2144, INF,  INF,  INF,  INF,},
            {INF, INF, INF,  INF, INF,  2364.6, INF, INF,  INF, INF,INF,INF, INF, INF,  INF,INF,INF, INF, INF,  INF,INF,INF, INF, INF,  INF,INF,  INF, INF,  INF, INF,2085,  0,INF, INF, INF,  2283.6,},
            {INF, INF,INF, INF,  2121.4, INF, INF, INF,  INF, INF,INF, INF, INF, INF, INF, INF,INF, INF, INF, INF,INF, INF,  INF, INF,INF, INF,  INF,  1807.2,INF,  INF,INF,  INF,  0,  2311.1,  INF, INF,},
            {INF, INF,  INF,INF, INF,  INF,INF, INF, INF, INF,INF, INF, INF, INF,INF, INF, INF, INF,INF, INF,INF,INF, INF, INF, INF,INF,  INF, INF, 2115.3, INF,INF, INF,  1999.5,  0,  2137.4,  INF,},
            {INF, INF,  INF,INF, INF,  INF,  2162.2, INF, INF,  INF,INF, INF, INF, INF,INF,  INF,INF, INF, INF, INF,INF, INF, INF,  INF,INF, INF, INF, INF,  INF,  2293.4,INF, INF,  INF,  2145.8,  0,INF,},
            {INF,  INF,INF, INF,INF,  INF, 2231,  INF, INF, INF,INF, INF, INF,  INF, INF,  INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,INF,INF, 2312.5, INF,  INF,  INF,  0 }};   //路段距离
	static double edge[][]=new double[D][D];  //最短距离
	static double dis[][]=new double[M][C];  //700个最短路径
	static int path[][]=new int[D][D]; 
	public static int begtocharge[]=new int[D];//第一辆车的最短路径
	public static int chargetodes[]=new int[D];

	static int outi;
	static int beg[] = {29, 19, 7, 11, 28, 28, 22, 21, 15, 20, 34, 23,  9, 28, 34, 31, 18, 20, 9, 19, 29,  10, 19, 10, 14, 29, 15, 34, 30, 15, 27, 12, 34, 28, 15, 19, 34, 17, 20, 11, 32, 21, 29, 14, 18, 15, 12, 34, 30, 26, 26, 35, 33, 35, 12, 11, 19, 13, 17, 29, 10, 33, 32, 27,  7, 10, 12, 24, 26, 34,  7,  7, 17, 26, 13, 20, 21, 35,  8, 35, 17, 23, 8, 23, 13, 32, 14, 21,  8, 13, 23, 31, 35, 34,  9, 23, 34,  8, 20, 26};  //100辆车的起点
	static int des[] = {19, 30, 19, 30, 7, 13, 30, 17, 29, 24, 28, 22, 30, 14, 21, 34, 15, 30, 20, 24, 34, 24, 23, 15, 9, 35, 27, 18, 34,  8, 31, 12, 17, 32, 20, 25,  9, 32, 12, 12, 29,  9,  9, 35, 19, 31, 10, 33, 14, 31, 34, 14, 15, 23, 34, 19, 30, 33, 15, 31, 27, 26, 12, 22, 34, 11, 28, 17, 15, 35, 19, 32, 34,  8, 17, 16, 20, 20, 11, 17, 32, 15, 21, 9, 29, 15, 21, 17, 18, 12, 15, 24, 12, 20, 34, 27, 30, 30, 26, 18 }; //100辆车的终点
	//int c[]=new int[C];    //充电站所在节点位置

	//粒子群的部分

	static final int pNum = 100;//粒子数量
	static final int dim = M;//车的数量
	static final int generation = 300;
	static final double low = -0.51;
	static final double high = 6.49;
	static final double vMax = 4;
	static final double w = 0.5;
	static final double c1 = 2;
	static final double c2 = 2;
	static double p[][]=new double[pNum][dim]; //粒子群体
	static int pt[][]=new int[pNum][dim];  //粒子的充电站信息
	static double pv[][]=new double[pNum][dim]; //速度向量 
	static double pBest[][]=new double[pNum][dim]; //每个粒子的局部最优向量
	static double pFitness[]=new double[pNum]; //每个粒子的最优最适值 
	static double gFitness; //全局最优适应值 
	static double gBest[]=new double[dim]; //全局最优向量
	static int g[]=new int[M]; //解
	
	static void yongdu_luwang()  //拥堵的路网信息
	{
		edge[13][18] = INF;
		edge[18][13] = INF;
		yongdu_edge[13][18] = 2;
		edge[17][18] = INF;
		edge[18][17] = INF;
		yongdu_edge[17][18] = 2;
		edge[28][29] = INF;
		edge[29][28] = INF;
		yongdu_edge[28][29] = 2;
		edge[29][30] = INF;
		edge[30][29] = INF;
		yongdu_edge[29][30] = 2;
		edge[19][18] += 5000;
		edge[18][19] += 5000;
		yongdu_edge[18][19] = 1;
		edge[3][18] += 5000;
		edge[18][3] += 5000;
		yongdu_edge[18][3] = 1;
		edge[28][27] += 5000;
		edge[27][28] += 5000;
		yongdu_edge[27][28] = 1;
	}
	
	//弗洛伊德

	static void initialize_edge()
	{
		for (int i = 0; i < D; i++)
			for (int j = 0; j < D; j++)
				edge[i][j] = aEdge[i][j];
	}

	static void floyd()
	{
		for (int k = 0; k < D; k++)
			for (int i = 0; i < D; i++)
				for (int j = 0; j < D; j++)
					if (edge[i][j] > edge[i][k] + edge[k][j]) {
						edge[i][j] = edge[i][k] + edge[k][j];
						path[i][j] = k;
					}
		}

	static void get_dis()
	{
		for (int i = 0; i < M; i++)
			for (int j = 0; j < C; j++) //{
				dis[i][j] = edge[beg[i]][j] + edge[j][des[i]];
	}

	//功能函数

	static int factorial(int x)  //阶乘
	{
		int sum;
		if (x == 0 || x == 1)
			return sum = 1;
		else
		{
			sum = 1;
			for (int i = 2; i <= x; i++)
				sum *= i;
			return sum;
		}
	}

	static double mgk_part(int x, int p[])
	{
		double sum = 0;
		for (int i = 0; i < cbar[x]; i++)
			sum += factorial(cbar[x] - 1)*(cbar[x] - lam[p[i]] * u) / (factorial(i)*Math.pow((double)lam[p[i]] * u, cbar[x] - 1));
		return sum;
	}

	//----------------------------------------------------------
	static void get_zuijin()
	{
		for (int i = 0; i < M; i++)
		{
			double temp = edge[des[i]][0];
			for (int j = 1; j < C; j++)
			{
				if (temp > edge[des[i]][j])
				{
					temp = edge[des[i]][j];
					zuijin[i] = j;
				}
			}
		}
	}

	static void get_reachablestation()
	{
		for(int i=0;i<M;i++)
			for (int j = 0; j < C; j++)
			{	
				if (edge[beg[i]][j] < SOC[i]*FULL*N && (edge[des[i]][zuijin[i]]+edge[j][des[i]]) < FULL*N)
				{
					reachablestation[i][j] = 1;
					reachablestationnum[i]++;
				}
			}
	}
	
	static void path_ini() {
		for(int i=0;i<D;i++)
			for(int j=0;j<D;j++)
				path[i][j]=-1;
	}
	
	static void getPathB_C(int i,int j) {
	    if(i==j) return;
	    if(path[i][j]==-1) {
	    	begtocharge[outi++]=j;
	    }
	    else{
	        getPathB_C(i,path[i][j]);
	        getPathB_C(path[i][j],j);
	    }
	}
	
	static void getPathC_D(int i,int j) {
	    if(i==j) return;
	    if(path[i][j]==-1) {
	    	chargetodes[outi++]=j;
	    }
	    else{
	        getPathC_D(i,path[i][j]);
	        getPathC_D(path[i][j],j);
	    }
	}
	//粒子群

	static double fitness(int po[],boolean b)
	{
		//---------------------------------------------------距离
		double sDis = 0;
		for (int i = 0; i < M; i++)
			sDis += dis[i][po[i]];

		//---------------------------------------------------时间
		double a[] = new double[M], chargeTime = 0, wait = 0;
		Random random=new Random();

		for (int i = 0; i < M; i++)
		{
			a[i] = Math.sqrt(delta) * random.nextGaussian()+u;
			chargeTime += a[i];
		}
		
		//等待时间，服从M/G/K模型
		for (int i = 0; i < M; i++)
			wait += (delta + u * u) / (2 * u*(cbar[po[i]] - lam[po[i]] * u)*(1 + mgk_part(po[i], po)));
		//总时间成本
		double sTime = sDis / v + chargeTime + wait;

		//----------------------------------------------------利用率
		double sUse = 0;
		double sum = 0, w = 0;
		int station[] = new int[C];
			for(int i=0;i<C;i++)
				station[i]=0;
		for (int i = 0; i < C; i++)
			for (int j = 0; j < M; j++)
				if (po[j] == i)
					station[i]++;
		for (int i = 0; i < C; i++)
			sum += cbar[i];
		for (int i = 0; i < C; i++)
		{
			w = P * station[i] - cbar[i] * P*M / sum;
			sUse += (w*w);
		}

		//----------------------------------------------------总
		if(b==true)
		return sDis + sTime + sUse;
		else {
			UI.ta.append( "总距离成本:"+sDis + "  总时间成本:" + sTime + "  总耗电成本:" + sUse + "\n" );
			UI.ta.append( "总成本:" + String.valueOf(sDis + sTime + sUse) + "\n");
			return 0;
		}
	}

	static void initialize()
	{
		Random rand = new Random();
		for (int i = 0; i < pNum; i++)
			for (int j = 0; j < dim; j++)
			{
				p[i][j] = low + (high - low) * 1.0 * rand.nextDouble();
				pBest[i][j] = p[1][j];
				pv[i][j] = -vMax + 2.0 * vMax * rand.nextDouble();
			}

		for (int i = 0; i < pNum; i++)
			for (int j = 0; j < dim; j++)
			{
				if(reachablestationnum[j]==0 && i==0)
				{
					UI.ta.append("第"+j+"辆车哪个充电站都去不了\n");
					continue;
				}
				if(reachablestationnum[j]==0)
					continue;
				int temp =(int)( p[i][j] + 0.5);
				int t= temp % reachablestationnum[j];
				int k=0;
				while (t>=0)
				{
					if (reachablestation[j][k] == 1)
						t--;
					k++;
				}
				pt[i][j] = --k;
			}

		for (int i = 0; i < pNum; i++)
			pFitness[i] = fitness(pt[i],true);

		gFitness = pFitness[0];
		for (int i = 0; i < dim; i++)
			gBest[i] = pBest[0][i];

		for (int i = 1; i < pNum; i++)
			if (gFitness > pFitness[i])
			{
				gFitness = pFitness[i];
				for (int j = 0; j < dim; j++)
					gBest[j] = pBest[i][j];
			}
	}

	static void particle_update()
	{
		Random rand = new Random();
		for (int i = 0; i < pNum; i++)
			for (int j = 0; j < dim; j++)
			{
				pv[i][j] = w * pv[i][j] + c1 * rand.nextDouble() * (pBest[i][j] - p[i][j]) + c2 * rand.nextDouble() * (gBest[j] - p[i][j]);
				if (pv[i][j] < -vMax)
					pv[i][j] = -vMax;
				if (pv[i][j] > vMax)
					pv[i][j] = vMax;
				p[i][j] = p[i][j] + pv[i][j];
				if (p[i][j] > high)
					p[i][j] = high;
				if (p[i][j] < low)
					p[i][j] = low;
			}
		for (int i = 0; i < pNum; i++)
			for (int j = 0; j < dim; j++)
			{
				int temp = (int)(p[i][j] + 0.5);
				if(reachablestationnum[j]==0)
				{
					continue;
				}
				int t= temp % reachablestationnum[j];
				int k=0;
				while (t>=0)
				{
					if (reachablestation[j][k] == 1)
						t--;
					k++;
				}
				pt[i][j] = --k;
			}
	}

	static void get_pBest()
	{
		for (int i = 0; i < pNum; i++)
			if (pFitness[i] > fitness(pt[i],true))
			{
				pFitness[i] = fitness(pt[i],true);
				for (int j = 0; j < dim; j++)//这里是适应度值
					pBest[i][j] = p[i][j];  //这里是粒子的位置信息
			}
	}

	static void get_gBest()
	{
		for (int i = 0; i < pNum; i++)
			if (pFitness[i] < gFitness)
			{
				gFitness = pFitness[i];
				for (int j = 0; j < dim; j++)
					gBest[j] = pBest[i][j];
			}
	}
	
	static void dataTest() {
		for(int i = 0;i<D;i++) {
			for(int j=0;j<D;j++) {
				if(edge[i][j]!=INF&&edge[i][j]!=0)
					UI.ta.append(i+","+j+"  ");
			}
			UI.ta.append("\n");
		}
	}

	public static void main(int carOneBegin, int carOneEnd) {
		beg[0] = carOneBegin;
		des[0] = carOneEnd;
		//dataTest();
		initialize_edge();
		if (UI.crowd.isSelected() == true)
		{
			//是在高峰期
			UI.ta.append("拥堵！\n注意：红色路段为非常拥堵，无法通行；橙色路段为拥堵，可以考虑通行\n");
			yongdu_luwang();
		} else {
			UI.ta.append("不拥堵！\n");
		}
		path_ini();
		floyd();
		get_dis();
		get_zuijin();
		get_reachablestation();
		//dataTest();
	
		//粒子群
	
		initialize();
		for (int i = 0; i < generation; i++)
		{
			particle_update();
			get_pBest();
			get_gBest();
		}
		UI.ta.append("一百辆车充电站选择：\n");
		for (int i = 0; i < dim; i++)
		{
			UI.ta.append((int)(gBest[i] + 0.5) + "    ");
			if (i % 10 == 9)
				UI.ta.append("\n");
		}
	
		for (int i = 0; i < M; i++)
			g[i] = (int)(gBest[i] + 0.5);
		fitness(g,false);
		
		UI.breakLine();
			
		for(int i=0;i<D;i++) {
			begtocharge[i]=INF;
			chargetodes[i]=INF;
		}
		outi=0;
		getPathB_C(carOneBegin,g[0]);
		outi=0;
		getPathC_D(g[0],carOneEnd);
	}
}
