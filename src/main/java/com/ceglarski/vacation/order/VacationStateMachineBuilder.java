package com.ceglarski.vacation.order;

import com.ceglarski.vacation.order.action.SendQuestion;
import com.ceglarski.vacation.order.action.SaveSupervisors;
import com.ceglarski.vacation.order.action.SendNotification;
import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.guard.DoesLeaderAccepted;
import com.ceglarski.vacation.order.guard.DoesManagerAccepted;
import com.ceglarski.vacation.order.guard.IsDateAvailable;
import com.ceglarski.vacation.order.state.OrderState;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

class VacationStateMachineBuilder {
    StateMachine<OrderState, OrderEvent> buildMachine() throws Exception {
        StateMachineBuilder.Builder<OrderState, OrderEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration().withConfiguration().listener(stateMachineListener()).autoStartup(false);

        builder
                .configureStates()
                .withStates()
                .initial(OrderState.CREATED)
                .choice(OrderState.IS_DATE_AVAILABLE)
                .state(OrderState.DATE_AVAILABLE)
                .state(OrderState.DATE_NOT_AVAILABLE, sendNotification(OrderState.DATE_NOT_AVAILABLE))
                .state(OrderState.SAVED_SUPERVISORS)
                .state(OrderState.ASK_LEADER)
                .state(OrderState.LEADER_WAS_ASKED)
                .choice(OrderState.DOES_LEADER_ACCEPTED)
                .state(OrderState.ASK_MANAGER)
                .state(OrderState.MANAGER_WAS_ASKED)
                .choice(OrderState.DOES_MANAGER_ACCEPTED)
                .state(OrderState.REJECTED, sendNotification(OrderState.REJECTED))
                .state(OrderState.ACCEPTED, sendNotification(OrderState.ACCEPTED))
                .end(OrderState.DATE_NOT_AVAILABLE)
                .end(OrderState.REJECTED)
                .end(OrderState.ACCEPTED);

        builder
                .configureTransitions()
                .withExternal().source(OrderState.CREATED).target(OrderState.IS_DATE_AVAILABLE).event(OrderEvent.START)
                //
                .and()
                //
                .withChoice()
                .source(OrderState.IS_DATE_AVAILABLE)
                .first(OrderState.DATE_AVAILABLE, isDateAvailable())
                .last(OrderState.DATE_NOT_AVAILABLE)
                .and()
                .withLocal().source(OrderState.DATE_AVAILABLE).target(OrderState.SAVED_SUPERVISORS)
                .action(saveSupervisors())
                .and()
//                .withLocal().source(OrderState.DATE_NOT_AVAILABLE).target(OrderState.REJECTED)
//                .and()
                .withLocal().source(OrderState.SAVED_SUPERVISORS).target(OrderState.ASK_LEADER)
                .and()
                .withLocal().source(OrderState.ASK_LEADER).target(OrderState.LEADER_WAS_ASKED)
                .action(sendQuestion())
                //
                .and()
                //
                .withExternal().source(OrderState.LEADER_WAS_ASKED).target(OrderState.DOES_LEADER_ACCEPTED).event(OrderEvent.LEADER_RESPONDED)
                //
                .and()
                //
                .withChoice()
                .source(OrderState.DOES_LEADER_ACCEPTED)
                .first(OrderState.ASK_MANAGER, doesLeaderAccepted())
                .last(OrderState.REJECTED)
                //
                .and()
                //
                .withLocal().source(OrderState.ASK_MANAGER).target(OrderState.MANAGER_WAS_ASKED)
                .action(sendQuestion())
                //
                .and()
                //
                .withExternal().source(OrderState.MANAGER_WAS_ASKED).target(OrderState.DOES_MANAGER_ACCEPTED).event(OrderEvent.MANAGER_RESPONDED)
                //
                .and()
                //
                .withChoice()
                .source(OrderState.DOES_MANAGER_ACCEPTED)
                .first(OrderState.ACCEPTED, doesManagerAccepted())
                .last(OrderState.REJECTED);

        return builder.build();
    }

    @Bean
    private SaveSupervisors saveSupervisors() {
        return new SaveSupervisors();
    }

    @Bean
    private SendNotification sendNotification(OrderState state) {
        return new SendNotification(state);
    }

    @Bean
    private SendQuestion sendQuestion() {
        return new SendQuestion();
    }

    @Bean
    private IsDateAvailable isDateAvailable() {
        return new IsDateAvailable();
    }

    @Bean
    private DoesLeaderAccepted doesLeaderAccepted() {
        return new DoesLeaderAccepted();
    }

    @Bean
    private DoesManagerAccepted doesManagerAccepted() {
        return new DoesManagerAccepted();
    }

    @Bean
    private StateMachineListener stateMachineListener() {
        return new StateMachineListener();
    }
}