//package com.ceglarski.vacation.order;
//
//import com.ceglarski.vacation.order.action.Ask;
//import com.ceglarski.vacation.order.action.SaveSupervisors;
//import com.ceglarski.vacation.order.action.SendNotification;
//import com.ceglarski.vacation.order.event.OrderEvent;
//import com.ceglarski.vacation.order.guard.IsDateAvailable;
//import com.ceglarski.vacation.order.state.OrderState;
//import lombok.extern.java.Log;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.statemachine.config.EnableStateMachineFactory;
//import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
//import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//
//
//class StateMachineConfiguration extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {
//
//    @Override
//    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
//        transitions
//                .withExternal().source(OrderState.CREATED).target(OrderState.IS_DATE_AVAILABLE)
//                //
//                .and()
//                //
//                .withChoice()
//                .source(OrderState.IS_DATE_AVAILABLE)
//                .first(OrderState.DATE_AVAILABLE, isDateAvailable())
//                .last(OrderState.DATE_NOT_AVAILABLE)
//                //
//                .and()
//                //
//                .withLocal().source(OrderState.DATE_AVAILABLE).target(OrderState.SAVED_SUPERVISORS).event(OrderEvent.GET_SUPERVISORS)
//                .action(saveSupervisors())
//                .and()
//                .withLocal().source(OrderState.DATE_NOT_AVAILABLE).target(OrderState.REJECTED)
//                .action(sendNotification())
//                .and()
//                //
//                .withLocal().source(OrderState.SAVED_SUPERVISORS).target(OrderState.LEADER_WAS_ASKED)
//                .action(ask());
//    }
//
//    @Override
//    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
//        states
//                .withStates()
//                .initial(OrderState.CREATED)
//                .choice(OrderState.IS_DATE_AVAILABLE)
//                .state(OrderState.DATE_AVAILABLE)
//                .state(OrderState.DATE_NOT_AVAILABLE)
//                .state(OrderState.SAVED_SUPERVISORS)
//                .end(OrderState.LEADER_WAS_ASKED)
//                .end(OrderState.REJECTED)
//                .end(OrderState.ACCEPTED);
//    }
//
//    @Override
//    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
//        config.withConfiguration()
//                .autoStartup(true);
//    }
//
//    @Bean
//    private SaveSupervisors saveSupervisors() {
//        return new SaveSupervisors();
//    }
//
//    @Bean
//    private SendNotification sendNotification() {
//        return new SendNotification();
//    }
//
//    @Bean
//    private Ask ask() {
//        return new Ask();
//    }
//
//    @Bean
//    private IsDateAvailable isDateAvailable() {
//        return new IsDateAvailable();
//    }
//}