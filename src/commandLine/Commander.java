package commandLine;

public class Commander extends Thread implements conveyor{
    private final String name;
    public Thread processing_semaphore;
    public Commander(String name, Thread semaphore){
        this.name = name;
        this.processing_semaphore=semaphore;
    }
    public void printAll(){
        for(String s:comm){
            System.out.print(s);
        }
    }
    private void nextCommand(){
        String command_raw = conveyor.comm.get(0).strip();
        String[] command_splited = command_raw.split("\\s+");
        String command_base = command_splited[0];
        String command_args = command_splited[1];
    }
    private int getLevenshteinDistance(String lhs, String rhs){
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        for (int i = 0; i < len0; i++) cost[i] = i;

        for (int j = 1; j < len1; j++) {
            newcost[0] = j;
            for(int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }
            int[] swap = cost; cost = newcost; newcost = swap;
        }
        return cost[len0 - 1];
    }
    @Override
    public void run(){
        while (processing_semaphore.isAlive()){

        }
    }
}
