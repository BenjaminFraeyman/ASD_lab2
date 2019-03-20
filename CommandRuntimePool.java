/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ibcn.gso.labo2;

/**
 *
 * @author Benjamin
 */
public class CommandRuntimePool implements ObjectPool<CommandRuntime>{
    private CommandRuntime[] pool = new CommandRuntime[4];
    public CommandRuntimePool(Engine engi){
        for (int i = 0; i < 4; i++) {   
            try{
                pool[i] = new CommandRuntime(engi);
            }
            catch(Exception e){
                System.out.println("error in commandruntimepool");
            }
        }
    }
    
    @Override
    public CommandRuntime getInstance() throws Exception {
        for(int i = 0; i < pool.length; i++){
            if(pool[i] != null && pool[i].isReady()){
                return pool[i];
            }
        }
        throw new RuntimeException("unable to use getinstance");
    }

    @Override
    public void releaseInstance(CommandRuntime instance) throws Exception {
        for(int i = 0; i < pool.length; i++){
            if(pool[i] == instance){
                pool[i].reset();
            }
        }
    }
    
}
