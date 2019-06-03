<script type="javascript">
function validacliente(){
nome = cadastrocliente.nome.value;
telefone = cadastrocliente.telefone.value;
endereco = cadastrocliente.endereco.value;

if(nome == ""){
alert("Campo nome obrigatório");
cadastrocliente.nome.focus();
return false;
}


}

</script>