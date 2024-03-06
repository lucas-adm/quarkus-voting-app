package domain;

import org.inferred.freebuilder.FreeBuilder;

import java.util.Optional;
import java.util.Set;

@FreeBuilder //Funciona como um Lombok
public interface CandidateQuery {

    Optional<Set<String>> ids();
    Optional<String> name ();

    //Rebuildar após a inserção desta classe.
    class Builder extends CandidateQuery_Builder {}

}
