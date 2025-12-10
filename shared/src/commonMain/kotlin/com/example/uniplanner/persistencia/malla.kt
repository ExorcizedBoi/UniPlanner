package com.example.uniplanner.persistencia

import com.example.uniplanner.models.Materia

object MallaData {
    val listaMaterias = listOf(
        // SEMESTRE 1
        Materia("INF-111", "Álgebra I", 1, 8, false, emptyList()),
        Materia("INF-112", "Cálculo I", 1, 8, false, emptyList()),
        Materia("INF-113", "Intr. Computación", 1, 6, true, emptyList()),
        Materia("INF-114", "Intr. Ingeniería", 1, 5, true, emptyList()),
        Materia("IFG-100", "Inglés I", 1, 3, true, emptyList()),

        // SEMESTRE 2
        Materia("INF-121", "Álgebra II", 2, 8, false, listOf("INF-111")),
        Materia("INF-122", "Cálculo II", 2, 8, false, listOf("INF-112")),
        Materia("INF-123", "Programación", 2, 8, true, listOf("INF-113")),
        Materia("INF-124", "Comprensión Lectora", 2, 3, true, listOf("INF-114")),
        Materia("IFG-200", "Inglés II", 2, 3, true, listOf("IFG-100")),

        // SEMESTRE 3
        Materia("INF-211", "Física I", 3, 6, false, emptyList()),
        Materia("INF-212", "Cálculo III", 3, 6, false, listOf("INF-122")),
        Materia("INF-213", "Estr. de Datos", 3, 6, false, listOf("INF-123")),
        Materia("INF-214", "Expr. Oral", 3, 3, true, listOf("INF-124")),
        Materia("INF-215", "Circuitos Digitales", 3, 6, true, listOf("INF-111")),
        Materia("IFG-300", "Inglés III", 3, 3, true, listOf("IFG-200")),

        // SEMESTRE 4
        Materia("INF-221", "Física II", 4, 6, false, listOf("INF-211")),
        Materia("INF-222", "Ecuaciones Dif.", 4, 5, false, listOf("INF-212")),
        Materia("INF-223", "Progra. Avanzada", 4, 5, false, listOf("INF-213")),
        Materia("INF-224", "Lógica Comp.", 4, 4, true, listOf("INF-111")),
        Materia("INF-225", "Arq. Computadores", 4, 5, true, listOf("INF-215")),
        Materia("INF-226", "Modulo integrador (Hito I)", 4, 5, true, listOf("INF-212", "INF-213", "INF-215")),

        // SEMESTRE 5
        Materia("INF-311", "Física III", 5, 5, false, listOf("INF-221")),
        Materia("INF-312", "Inf. Estadística", 5, 6, true, listOf("INF-222")),
        Materia("INF-313", "Estr. Discretas", 5, 5, true, listOf("INF-223")),
        Materia("INF-314", "Modelamiento Datos", 5, 5, true, listOf("INF-223")),
        Materia("INF-315", "Taller Circuitos", 5, 4, true, listOf("INF-225")),
        Materia("MFG-114", "Intr. a la Fe", 5, 5, true, emptyList()),

        // SEMESTRE 6
        Materia("INF-321", "Comp. Numérica", 6, 4, true, listOf("INF-222")),
        Materia("INF-322", "Inv. Operaciones", 6, 5, true, listOf("INF-312")),
        Materia("INF-323", "Teoría Comp.", 6, 5, true, listOf("INF-224")),
        Materia("INF-324", "Base de Datos", 6, 6, true, listOf("INF-314")),
        Materia("INF-325", "Sist. Operativos", 6, 5, true, listOf("INF-315")),
        Materia("MFG-216", "Ética Cristiana", 6, 5, true, listOf("MFG-114")),

        // SEMESTRE 7
        Materia("INF-411", "Metodología Inv.", 7, 6, true, listOf("INF-226")),
        Materia("INF-412", "Economía", 7, 6, true, listOf("INF-226")),
        Materia("INF-413", "Algoritmos", 7, 6, true, listOf("INF-313")),
        Materia("INF-414", "Sist. Información", 7, 6, true, listOf("INF-324")),
        Materia("INF-415", "Redes y Datos", 7, 6, true, listOf("INF-325")),

        // SEMESTRE 8
        Materia("INF-421", "Mód. Int. de Licenciatura", 8, 8, true, listOf("INF-411","412","413", "INF-414", "INF-415")),
        Materia("INF-422", "Contab. y Finanzas", 8, 4, true, listOf("INF-412")),
        Materia("INF-423", "Int. Artificial", 8, 4, true, listOf("INF-413")),
        Materia("INF-424", "Ing. Software I", 8, 5, true, listOf("INF-414")),
        Materia("INF-425", "Redes Avanzadas", 8, 4, true, listOf("INF-415")),
        Materia("CFG-1",   "Certificación I", 8, 5, true, emptyList()),

        // SEMESTRE 9
        Materia("INF-511", "Eval. Proyectos", 9, 5, true, listOf("INF-422")),
        Materia("INF-512", "Gestión Inf. I", 9, 5, true, listOf("INF-422")),
        Materia("INF-513", "Calidad SW", 9, 5, true, listOf("INF-424")),
        Materia("INF-514", "Ing. Software II", 9, 5, true, listOf("INF-424")),
        Materia("INF-515", "Sist. Distribuidos", 9, 5, true, listOf("INF-415")), // Req red
        Materia("CFG-2",   "Certificación II", 9, 5, true, listOf("CFG-1")),

        // SEMESTRE 10
        Materia("INF-521", "RRHH y Legislación", 10, 5, true, emptyList()),
        Materia("INF-522", "Gestión Inf. II", 10, 5, true, listOf("INF-512")),
        Materia("INF-523", "Electivo I", 10, 5, true, emptyList()),
        Materia("INF-524", "Taller Des. SW", 10, 5, true, listOf("INF-514")),
        Materia("INF-525", "Seguridad Inf.", 10, 5, true, listOf("INF-425")),
        Materia("CFG-3",   "Certificación III", 10, 5, true, listOf("CFG-2")),

        // SEMESTRE 11
        Materia("INF-611", "Electivo II", 11, 5, true, listOf("INF-421")),
        Materia("INF-612", "Electivo III", 11, 5, true, listOf("INF-421")),
        Materia("INF-613", "Práctica Profesional", 11, 20, true, listOf("INF-524", "INF-522"))
    )
}