package sube.interviews.mareoenvios.entity;

import java.util.*;

public enum State {
    INICIAL,
    ENTREGADO_AL_CORREO,
    EN_CAMINO,
    CANCELADO,
    ENTREGADO;

    private static final Map<State, List<State>> VALID_TRANSITIONS = new HashMap<>();

    static {
        VALID_TRANSITIONS.put(INICIAL, Arrays.asList(ENTREGADO_AL_CORREO, CANCELADO));
        VALID_TRANSITIONS.put(ENTREGADO_AL_CORREO, Arrays.asList(EN_CAMINO, CANCELADO));
        VALID_TRANSITIONS.put(EN_CAMINO, Arrays.asList(ENTREGADO));
        VALID_TRANSITIONS.put(CANCELADO, Collections.emptyList());
        VALID_TRANSITIONS.put(ENTREGADO, Collections.emptyList());
    }

    public boolean canTransitionTo(State nextState) {
        return VALID_TRANSITIONS.getOrDefault(this, Collections.emptyList()).contains(nextState);
    }
}
