const express = require("express");
const router = express.Router();
const { sendMessage, listar_minhas_mensagens } = require("../service/MensagensService");
const jwt = require("jsonwebtoken");

// const User = require("../models/mysqlmodels/User");
// const MensagensSchema = require("../models/mongodbmodels/MensagensSchema"); // importa o model
const UserModel = require("../models/mysqlmodels/User")
function checkToken(req, res, next) {
  const authHEaderv = req.headers["authorization"];

  const token = authHEaderv && authHEaderv.split(" ")[1];

  if (!token) {
    return res.status(403).json({ message: "acesso negado" });
  }

  try {
    const decoded = jwt.verify(token, process.env.SECRET);
    req.userToken = decoded;
    
    next();
  } catch (error) {
    res.status(400).json({ message: "token invalido" } + error);
  }
}

async function checkIdToken(req, res, next) {
  try {
    const userId = req.userToken.sub;

    const existUser = await UserModel.findOne({ where: { id_usuario: userId } });

    
    if (!existUser) {
      res.status(400).json({ message: "usuario não existe ou token invalido" });
    }
    next();
  } catch (error) {
    res.status(500).json({ message: "Erro interno" + error });
  }
}



router.post("/", checkToken, async (req, res) => {
  const message = {
    receiver: req.body.receiver,
    data: req.body.data,
    message: req.body.message,
  };

  console.log(message);
  const results = await sendMessage(
    req.headers["authorization"].split(" ")[1],
    message
  );
  if (results.sucesso) {
    res.status(results.status_code).json({ data: results.data });
  } else {
    res.status(results.status_code).json({ error: results.message });
  }
});

router.get("/:id_solicitacao", checkToken, checkIdToken, async (req, res) => {
  
  const id_solicitacao = req.params.id_solicitacao
  const results = await listar_minhas_mensagens(id_solicitacao);

  if (results.sucesso) {
    res.status(results.status_code).json(results.data);
  } else {
    res.status(results.status_code).json({ error: results.message });
  }
});

module.exports = router;
