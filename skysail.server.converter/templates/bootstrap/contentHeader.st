contentHeader(converter, messages, model) ::= <<
  $if(converter.edit)$
  $model.test$-
    <a href='/i18n/v1/messages/$model.resourceClassName$.message/'>$if(messages.("content.header").translated)$$messages.("content.header").translated$$else$___$endif$</a>
  $else$
$messages.("content.header").translated$
  $endif$
>>