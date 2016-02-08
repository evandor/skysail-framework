contentHeader(converter, messages, model) ::= <<
  $if(converter.edit)$
    <a href='/i18n/v1/messages/$model.classname$.message/'>$if(messages.("content.header").translated)$$messages.("content.header").translated$$else$___$endif$</a>
  $else$
$messages.("content.header").translated$
  $endif$
>>