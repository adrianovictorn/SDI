A classe que implementa o relógio de Lamport é responsável por manter e atualizar o contador lógico de cada nó. Sempre que um evento local ocorre ou uma mensagem é enviada, o método de incremento é chamado para aumentar o contador. Quando receber uma mensagem, o método de atualização compara o timestamp recebido com o tempo atual e ajusta o contador para ser o maior valor entre os dois, incrementado de um.

A classe Mensagem encapsula o conteúdo que será enviado entre os nós, além do timestamp gerado no momento do envio. Esse timestamp representa o instante lógico em que a mensagem foi originada, garantindo que o nó destinatário possa usar esse valor para atualizar seu próprio relógio. Assim, o envio e a recepção das mensagens se tornam eventos sincronizados, permitindo rastrear a ordem dos acontecimentos de forma coerente.

Parte 4: Gerenciamento dos Nós (No)
Cada nó do sistema é representado pela classe No, que contém:

Um nome para identificar o nó;
Um objeto do tipo RelogioLamport para gerenciar o contador lógico;
Uma fila para armazenar as mensagens recebidas.
O nó pode executar eventos locais, onde seu contador é incrementado, e enviar mensagens, nas quais o timestamp atual é incluído. Ao receber uma mensagem, o nó atualiza seu relógio utilizando o timestamp da mensagem e armazena a mensagem para processamento posterior. Dessa forma, os nós conseguem manter a ordem dos eventos mesmo em um ambiente distribuído.

Parte 5: Ponto de Entrada (Principal)
A classe Principal serve como o ponto de entrada da aplicação. Nela, são criados três nós que simulam um cenário de comunicação distribuída. Cada nó executa um evento local inicial (como “Iniciar computação” ou “Carregar dados”) e, em seguida, ocorre a troca de mensagens entre eles. Por fim, cada nó processa as mensagens recebidas, demonstrando a sincronização e a ordenação dos eventos conforme os valores lógicos de seus relógios.

Conclusã