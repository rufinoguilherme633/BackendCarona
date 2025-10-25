require("../models/AnuncioSchema");
const mongoose = require("mongoose");
const bcrypt = require("bcrypt");
const AnuncioSchema = mongoose.model("anuncio");
const { gerarToken, decodificarToken } = require("./TokenService");

const login = async (login) => {
      if(!login.email ||typeof login.email === undefined || login.email === null){
        console.log("email não pode ser vazio")
        return {sucesso: false, status_code: 400,message: "email não pode ser vazio" };
    }
     if(!login.senha ||typeof login.senha === undefined || login.senha === null){
        console.log("senha não pode ser vazio")
         return { sucesso: false, status_code: 400,message: "senha não pode ser vazio" };
    }

  const user = await AnuncioSchema.findOne({ email: login.email });
 
  if (!user) {
    console.log("Usuario não encontrado");
    return { sucesso: false, status_code: 404,message: "Usuario não encontrado" };
  }
  const checkPassword = await bcrypt.compare(login.senha, user.senha);

  if (!checkPassword) {
    return { sucesso: false,status_code: 400, message: "senha invalida" };
  }

  try {
    const payload = {
      id: user._id,
      email: user.email,
    };

    const token = gerarToken(payload)

    return { sucesso: true,status_code: 200, data: token };
  } catch (error) {
    console.log(error);
    return { sucesso: false, status_code: 500,message: error.message };
  }
};

const cadastrar = async (cadastro) => {
  console.log(cadastro)

  if (
    !cadastro.nome_dono ||
    typeof cadastro.nome_dono === undefined ||
    cadastro.nome_dono === null
  ) {
    console.log("nome não pode ser vazio");
    return { sucesso: false, status_code: 400,message: "nome_dono não pode ser vazio" };
  } else if (
    !cadastro.logo ||
    typeof cadastro.logo === undefined ||
    cadastro.logo === null
  ) {
    console.log("logo não pode ser vazio");
    return { sucesso: false, status_code: 400, message: "logo não pode ser vazio" };
  } else if (
    !cadastro.razao_social ||
    typeof cadastro.razao_social === undefined ||
    cadastro.razao_social === null
  ) {
    console.log("razao_social não pode ser vazio");
    return { sucesso: false, status_code: 400,message: "razao_social não pode ser vazio" };
  } else if (
    !cadastro.nome_fantasia ||
    typeof cadastro.nome_fantasia === undefined ||
    cadastro.nome_fantasia === null
  ) {
    console.log("nome_fantasia não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "nome_fantasia não pode ser vazio" };
  } else if (
    !cadastro.cnpj ||
    typeof cadastro.cnpj === undefined ||
    cadastro.cnpj === null
  ) {
    console.log("cnpj não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "cnpj não pode ser vazio" };
  } else if (
    !cadastro.ramo_empresa ||
    typeof cadastro.ramo_empresa === undefined ||
    cadastro.ramo_empresa === null
  ) {
    console.log("ramo_empresa não pode ser vazio");
    return { sucesso: false, status_code: 400, message: "ramo_empresa não pode ser vazio" };
  } else if (
    !cadastro.descricao_anuncio ||
    typeof cadastro.descricao_anuncio === undefined ||
    cadastro.descricao_anuncio === null
  ) {
    console.log("descricao_anuncio não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "descricao_anuncio não pode ser vazio" };
  } else if (
    !cadastro.endereço_empresa ||
    typeof cadastro.endereço_empresa === undefined ||
    cadastro.endereço_empresa === null
  ) {
    console.log("endereço_dono não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "endereço_empresa não pode ser vazio" };
  } else if (
    !cadastro.endereço_dono ||
    typeof cadastro.endereço_dono === undefined ||
    cadastro.endereço_dono === null
  ) {
    console.log("endereço_dono não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "endereço_dono não pode ser vazio" };
  } else if (
    !cadastro.contato ||
    typeof cadastro.contato === undefined ||
    cadastro.contato === null
  ) {
    console.log("contato não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "contato não pode ser vazio" };
  } else if (
    !cadastro.email ||
    typeof cadastro.email === undefined ||
    cadastro.email === null
  ) {
    console.log("email não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "email não pode ser vazio" };
  } else if (
    !cadastro.senha ||
    typeof cadastro.senha === undefined ||
    cadastro.senha === null
  ) {
    console.log("senha não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "senha não pode ser vazio" };
  } else if (
    !cadastro.anuncio ||
    typeof cadastro.anuncio === undefined ||
    cadastro.anuncio === null
  ) {
    console.log("anuncio não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "anuncio não pode ser vazio" };
    turn;
  } else if (
    !cadastro.quantidade_alcance ||
    typeof cadastro.quantidade_alcance === undefined ||
    cadastro.quantidade_alcance === null
  ) {
    console.log("quantidade_alcance não pode ser vazio");
    return { sucesso: false,  status_code: 400,message: "quantidade_alcance não pode ser vazio" };
  }

  const exist_razao_social = await AnuncioSchema.findOne({
    razao_social: cadastro.razao_social,
  });

  if (exist_razao_social) {
    console.log("razao_social já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:400,
      message: "razao_social já pertence a outro usuario do sistema",
    };
  }
  const existCnpj = await AnuncioSchema.findOne({ cnpj: cadastro.cnpj });

  if (existCnpj) {
    console.log("cnpj já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:400,
      message: "cnpj já pertence a outro usuario do sistema",
    };
  }

  const existEmail = await AnuncioSchema.findOne({ email: cadastro.email });

  if (existEmail) {
    console.log("email já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:400,
      message: "email já pertence a outro usuario do sistema",
    };
  }

  const existContato = await AnuncioSchema.findOne({
    contato: cadastro.contato,
  });

  if (existContato) {
    console.log("contato já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:400,
      message: "contato já pertence a outro usuario do sistema",
    };
  }

  const salt = await bcrypt.genSalt(12);
  const passwordHash = await bcrypt.hash(cadastro.senha, salt);
  cadastro.senha = passwordHash;

  data ={
  nome_dono : cadastro.nome_dono,
  logo : cadastro.logo,
  razao_social : cadastro.razao_social,
  nome_fantasia : cadastro.nome_fantasia,
  cnpj : cadastro.cnpj,
  ramo_empresa : cadastro.ramo_empresa,
  descricao_anuncio : cadastro.descricao_anuncio,
  endereço_empresa : cadastro.endereço_empresa,
  endereço_dono : cadastro.endereço_dono,
  contato :cadastro.contato,
  email : cadastro.email,
  senha :cadastro.senha,
  anuncio :cadastro.anuncio,
  quantidade_alcance : cadastro.quantidade_alcance,
  quantidade_alcancados:0

}

  try {
    const new_anuncio = await new AnuncioSchema(data).save();
    return { sucesso: true, status_code:200,data: new_anuncio };
  } catch (error) {
    console.log(error);
    return { sucesso: false,status_code:500, message: error.message };
  }
};

const divulgar = async () => {
  try {
    console.log("aqui");
    const anuncio = await AnuncioSchema.aggregate([
      {
        $match: {
          $expr: { $gt: ["$quantidade_alcance", "$quantidade_alcancados"] },
        },
      },
      { $sample: { size: 1 } },
      {$project:{"anuncio":1,"quantidade_alcancados": 1}}
    ]);

    console.log(anuncio)
    const quantidade_alcancados = anuncio[0].quantidade_alcancados + 1;

    console.log("quantidade_alcancandos "+quantidade_alcancados)

    await AnuncioSchema.updateOne(
      { _id: anuncio[0]._id },
      { $set: { quantidade_alcancados: quantidade_alcancados } }
    );

    return { sucesso: true,status_code:200, data: anuncio };
  } catch (error) {
    console.log(error.message);
    return { sucesso: false,status_code:500, message: error.message };
  }
};

const atualizar_anuncio = async (anuncio, token) => {


  const dados = decodificarToken(token);

  const existUser = await AnuncioSchema.findOne({ _id: dados.id });

  if (!existUser) {
    return { sucesso: false,status_code:404, message: "usuario inexistente" };
  }

  if (
    !anuncio.nome_dono ||
    typeof anuncio.nome_dono === undefined ||
    anuncio.nome_dono === null
  ) {
    console.log("nome não pode ser vazio");
    return { sucesso: false,status_code:400, message: "nome_fantasia não pode ser vazio" };
  } else if (
    !anuncio.logo ||
    typeof anuncio.logo === undefined ||
    anuncio.logo === null
  ) {
    console.log("logo não pode ser vazio");
    return { sucesso: false,status_code:400, message: "logo não pode ser vazio" };
  } else if (
    !anuncio.razao_social ||
    typeof anuncio.razao_social === undefined ||
    anuncio.razao_social === null
  ) {
    console.log("razao_social não pode ser vazio");
    return { sucesso: false,status_code:400, message: "razao_social não pode ser vazio" };
  } else if (
    !anuncio.nome_fantasia ||
    typeof anuncio.nome_fantasia === undefined ||
    anuncio.nome_fantasia === null
  ) {
    console.log("nome_fantasia não pode ser vazio");
    return { sucesso: false, status_code:400,message: "nome_fantasia não pode ser vazio" };
  } else if (
    !anuncio.cnpj ||
    typeof anuncio.cnpj === undefined ||
    anuncio.cnpj === null
  ) {
    console.log("cnpj não pode ser vazio");
    return { sucesso: false,status_code:400, message: "cnpj não pode ser vazio" };
  } else if (
    !anuncio.ramo_empresa ||
    typeof anuncio.ramo_empresa === undefined ||
    anuncio.ramo_empresa === null
  ) {
    console.log("ramo_empresa não pode ser vazio");
    return { sucesso: false,status_code:400, message: "ramo_empresa não pode ser vazio" };
  } else if (
    !anuncio.descricao_anuncio ||
    typeof anuncio.descricao_anuncio === undefined ||
    anuncio.descricao_anuncio === null
  ) {
    console.log("descricao_anuncio não pode ser vazio");
    return { sucesso: false,status_code:400, message: "descricao_anuncio não pode ser vazio" };
  } else if (
    !anuncio.endereço_empresa ||
    typeof anuncio.endereço_empresa === undefined ||
    anuncio.endereço_empresa === null
  ) {
    console.log("endereço_dono não pode ser vazio");
    return { sucesso: false,status_code:400, message: "endereço_empresa não pode ser vazio" };
  } else if (
    !anuncio.endereço_dono ||
    typeof anuncio.endereço_dono === undefined ||
    anuncio.endereço_dono === null
  ) {
    console.log("endereço_dono não pode ser vazio");
    return { sucesso: false,status_code:400, message: "endereço_dono não pode ser vazio" };
  } else if (
    !anuncio.contato ||
    typeof anuncio.contato === undefined ||
    anuncio.contato === null
  ) {
    console.log("contato não pode ser vazio");
    return { sucesso: false,status_code:400, message: "contato não pode ser vazio" };
  } else if (
    !anuncio.email ||
    typeof anuncio.email === undefined ||
    anuncio.email === null
  ) {
    console.log("email não pode ser vazio");
    return { sucesso: false,status_code:400, message: "email não pode ser vazio" };
  } else if (
    !anuncio.senha ||
    typeof anuncio.senha === undefined ||
    anuncio.senha === null
  ) {
    console.log("senha não pode ser vazio");
    return { sucesso: false, status_code:400,message: "senha não pode ser vazio" };
  } else if (
    !anuncio.anuncio ||
    typeof anuncio.anuncio === undefined ||
    anuncio.anuncio === null
  ) {
    console.log("anuncio não pode ser vazio");
    return { sucesso: false,status_code:400, message: "anuncio não pode ser vazio" };
    
  } else if (
    !anuncio.quantidade_alcance ||
    typeof anuncio.quantidade_alcance === undefined ||
    anuncio.quantidade_alcance === null
  ) {
    console.log("quantidade_alcance não pode ser vazio");
    return { sucesso: false,status_code:400, message: "quantidade_alcance não pode ser vazio" };
  }

  const exist_razao_social = await AnuncioSchema.findOne({
    razao_social: anuncio.razao_social,
  });



  if (exist_razao_social && String(exist_razao_social._id) != String(existUser._id)) {
    console.log(exist_razao_social._id);
    console.log(existUser._id);
    console.log("razao_social já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:409,
      message: "razao_social já pertence a outro usuario do sistema",
    };
  }
  const existCnpj = await AnuncioSchema.findOne({ cnpj: anuncio.cnpj });

  if (existCnpj &&  String(existCnpj._id) != String(existUser._id)) {
    console.log("cnpj já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:409,
      message: "cnpj já pertence a outro usuario do sistema",
    };
  }

  const existEmail = await AnuncioSchema.findOne({ email: anuncio.email });

  if (existEmail && String(existEmail._id) != String(existUser._id)) {
    console.log("email já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:409,
      message: "email já pertence a outro usuario do sistema",
    };
  }

  const existContato = await AnuncioSchema.findOne({
    contato: anuncio.contato,
  });

  if (existContato && String(existContato._id) != String(existUser._id)) {
    console.log("contato já pertence a outro usuario do sistema");
    return {
      sucesso: false,
      status_code:409,
      message: "contato já pertence a outro usuario do sistema",
    };
  }

  const salt = await bcrypt.genSalt(12);

  const passwordHash = await bcrypt.hash(anuncio.senha, salt);
  anuncio.senha = passwordHash;

  existUser.nome_dono = anuncio.nome_dono;
  existUser.logo = anuncio.logo;
  existUser.razao_social = anuncio.razao_social;
  existUser.nome_fantasia = anuncio.nome_fantasia;
  existUser.cnpj = anuncio.cnpj;
  existUser.ramo_empresa = anuncio.ramo_empresa;
  existUser.descricao_anuncio = anuncio.descricao_anuncio;
  existUser.endereço_empresa = anuncio.endereço_empresa;
  existUser.endereço_dono = anuncio.endereço_dono;
  existUser.contato = anuncio.contato;
  existUser.email = anuncio.email;
  existUser.senha = anuncio.senha;
  existUser.anuncio = anuncio.anuncio;
  existUser.quantidade_alcance = anuncio.quantidade_alcance;

  try {
    existUser.save();
    console.log("aqui");
    console.log(existUser.toJSON())
    let atualizado = existUser.toJSON()
    return { sucesso: true, status_code:200 ,data: {message:"tudo ok", "dados":atualizado}  };
  } catch (error) {
    console.log("erro ao atualizar dados");
    return { sucesso: false, message: "error" + error.message };
  }
};



const deletar =async (token) =>{

  const dados =  decodificarToken(token);
  console.log("dados" + dados)
  const user = await AnuncioSchema.findOne({_id:dados.id});
  console.log(user)

  if(!user){
    console.log("nenhum usuario encontrado")
    return {secesso:false,status_code:404,message:"nenhum usuario encontrado"}
  }


  const data = await AnuncioSchema.deleteOne({_id:user.id}).then(()=>{
  return {sucesso:true,status_code:200,data:"deletado com sucesso"}

  }).catch((error)=>{
  return {sucesso:false,status_code:500,data:"Erro " + error}

  })

  return data;





}
module.exports = {
  cadastrar,
  divulgar,
  login,
  atualizar_anuncio,
  deletar
};
