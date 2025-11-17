const MensagensSchema = require("../models/mongodbmodels/MensagensSchema"); // importa o model
const { decodificarToken } = require("./TokenService");
const { findById } = require("./UserService");

const sendMessage = (token, message) => {
  const dados = decodificarToken(token);
  const existUser = findById(dados.sub);
  const existReciver = findById(message.receiver);

  //validar solicitacao

  if (!existUser) {
    return {
      sucesso: false,
      status_code: 400,
      message: "Nenhum usuario encontrado",
    };
  } else if (!existReciver) {
    return {
      sucesso: false,
      status_code: 400,
      message: "Nenhum usuario receptor encontrado",
    };
  }

  try {
    const messageData = {
      id_sender: dados.sub,
      id_receiver: message.receiver,
      id_solicitacao: message.id_solicitacao,
      data: message.data,
      data_atualizacao: null,
      message: message.message,
    };
    const messageSave = new MensagensSchema(messageData);
    messageSave.save();
    return {
      sucesso: true,
      status_code: 200,
      data: "mensagem salva com sucesso",
    };
  } catch (error) {
    return {
      sucesso: false,
      status_code: 400,
      message: "não foi possivel salvar mensagem " + error,
    };
  }
};

const listar_minhas_mensagens = async (id_solicitacao) => {
  if (
    id_solicitacao == null,
    typeof id_solicitacao == undefined,
    !isNaN(id_solicitacao),
    id_solicitacao === ""
  ) {
    return {
      sucesso: false,
      status_code: 500,
      message: "id da solicitação invalido,, não pode ser nulo indefinido e ",
    };
  }

  const minhas_conversas = await MensagensSchema.find({
    id_solicitacao: id_solicitacao,
  });

  if (minhas_conversas.length <= 0) {
    return {
      sucesso: true,
      status_code: 401,
      data: "nenhuma conversa encontrada",
    };
  }
  return { sucesso: true, status_code: 200, data: minhas_conversas };
};

module.exports = {
  sendMessage,
  listar_minhas_mensagens
};
