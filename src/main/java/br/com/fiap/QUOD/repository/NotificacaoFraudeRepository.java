package br.com.fiap.QUOD.repository;

import br.com.fiap.QUOD.model.NotificacaoFraude;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface NotificacaoFraudeRepository extends MongoRepository<NotificacaoFraude, UUID> {
}
