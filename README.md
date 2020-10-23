# Calculator-socket-multithreaded

  ## Sobre

  Calculadora que se conecta em diferentes servidores, um para operações básicas e outro para operações especiais
  * Operações básicas:
    * Soma +;
    * Subtração -;
    * Divisão /;
    * Multiplicação *;
    
  * Operações Especiais:
    * Porcentagem %;
    * Exponenciação ^;
    * Radiciação #;
    
  ### Detalhes da Sintaxe
  
  * Como funciona:
    * \+ = Soma N1 com N2
    * \- = Subtrai N2 de N1
    * \* = Multiplica N1 e N2
    * \/ = Divide N1 por N2
    * \% = N1 por cento de N2
    * \^ = N1 elevado por N2
    * \# = N1 índice da raiz de N2
    
  * Regras:
    * Para operações básicas digite: N1 +|-|*|/ N2
    * Para operações especiais digite: N1 %|^|# N2
    * Sempre separado por espaços: N1 op N2
    * Usar formato de decimal com . (Ex.: 2.58)
    * Só uma operação por vez
    
  ## Request e Response
  
  ### Request:
  
  * É enviado ao servidor no formato: "N1 OP N2";
    * N1 é o número 1
    * OP é o operador
    * N2 é o número 2
  
  * Patterns regex que garante formatos->01, 1.0, 10.01:
    * N1 = ^\d+(\.\d+)?
    * N2 = ^\d+(\.\d+)?
    
  ### Response:
  
  * Os servidores respondem com padrão: "expression \r\n result";
    * expression é a expressão enviada pelo cliente
    * \r - carriage return
    * \n - new line
    * result é o resultado da expressão tratado pelo servidor

  ## Adendos

  * Servidor
    * Ao rodar eles mostram a porta em que foram abertos.
    * Trata expressões com erro de sintaxe.

  * Cliente
    * Mostra uma interface agradável para uso.
    * Possui histórico de operações.
    * Indica para qual servidor a solicitação foi enviada.

  ## Referências utilizadas:

  * https://www.devmedia.com.br/como-criar-um-chat-multithread-com-socket-em-java/33639
  * https://docs.oracle.com/en/java/javase/11/
  * https://regexr.com
