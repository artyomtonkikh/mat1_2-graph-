import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

class mybehaviour extends Behaviour {
    boolean is_alive = false;
    myagent agent;
    int amount_of_agents=7;
    mybehaviour(myagent agent){
        this.agent=agent;
    }
    @Override
    public void action() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(agent.messages>=3*amount_of_agents){
            getMean();
        }
        agent.messages++;
        for (int i=0;i<agent.nodes.size();i++){
            while(agent.receive()!=null)
                getMessage();
        }
        for (int i=0;i<agent.nodes.size();i++){
            getMessage();
            ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Agent"+String.valueOf(agent.nodes.get(i)), AID.ISLOCALNAME));
            String text=agent.list_of_agents;
            text=text+";"+String.valueOf(agent.list_of_numbers);
            msg.setContent(String.valueOf(text));
            agent.send(msg);
        }
    }

    @Override
    public boolean done() {
        return is_alive;
    }
    protected void getMessage(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //is_alive = false;
        ACLMessage msg=agent.receive();
        while(msg!=null) {
            boolean exist=false;
            AID name_receiver= msg.getSender();
            String message=msg.getContent();
            String[] split_message=message.split(";");
            String indexes =split_message[0];
            String numbers=split_message[1];
            String[] indexes_massive=indexes.split(",");
            String[] numbers_massive=numbers.split(",");
            String[] split_list_agents=agent.list_of_agents.split(",");
            for (int i=0;i<indexes_massive.length;i++){
                exist=false;
                for (int j=0;j<split_list_agents.length;j++){
                    if(Objects.equals(indexes_massive[i],split_list_agents[j])==true){
                        exist=true;
                        break;
                    }
                }
                if(!exist){
                    agent.list_of_agents=agent.list_of_agents+","+indexes_massive[i];
                    agent.list_of_numbers=agent.list_of_numbers+","+numbers_massive[i];
                }
            }
            //System.out.println(agent.getLocalName()+" have "+agent.list_of_numbers);
            msg=agent.receive();
        }
    }

    private void getMean() {
        String str_numbers=agent.list_of_numbers;
        String[] list_numbers=str_numbers.split(",");
        double summ=0;
        for (int i=0;i< list_numbers.length;i++)
        {
            summ+=Double.valueOf(list_numbers[i]);
        }
        System.out.println("!!!!!!!!!!!!!!!!!!"+agent.getLocalName()+" got "+ agent.list_of_numbers+ " and ariphmetic mean is: "+summ/list_numbers.length+"!!!!!!!!!!!!!!!!!!");
        is_alive=true;
    }
}
/*точно рабочий класс
public class mybehaviour extends Behaviour {
    boolean is_alive = false;

    @Override
    public void action() {
        System.out.println("hello world");
        is_alive=true;
    }

    @Override
    public boolean done() {
        return is_alive;
    }
}*/
