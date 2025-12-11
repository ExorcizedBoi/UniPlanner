package com.example.uniplanner.persistencia

import com.example.uniplanner.models.Ramo
import com.example.uniplanner.models.Modulos
import com.example.uniplanner.models.RequisitosAcademicos

object MallaData {
    val listaMaterias: List<RequisitosAcademicos> = listOf(
        // SEMESTRE 1
        Ramo("INF-111", "Álgebra I", 8, false, emptyList(), 1),
        Ramo("INF-112", "Cálculo I", 8, false, emptyList(), 1),
        Ramo("INF-113", "Intr. Computación", 6, true, emptyList(), 1),
        Ramo("INF-114", "Intr. Ingeniería", 5, true, emptyList(), 1),
        Ramo("IFG-100", "Inglés I", 3, true, emptyList(), 1),

        // SEMESTRE 2
        Ramo("INF-121", "Álgebra II", 8, false, listOf("INF-111"), 2),
        Ramo("INF-122", "Cálculo II", 8, false, listOf("INF-112"), 2),
        Ramo("INF-123", "Programación", 8, true, listOf("INF-113"), 2),
        Ramo("INF-124", "Comprensión Lectora", 3, true, listOf("INF-114"), 2),
        Ramo("IFG-200", "Inglés II", 3, true, listOf("IFG-100"), 2),

        // SEMESTRE 3
        Ramo("INF-211", "Física I", 6, false, emptyList(), 3),
        Ramo("INF-212", "Cálculo III", 6, false, listOf("INF-122"), 3),
        Ramo("INF-213", "Estr. de Datos", 6, false, listOf("INF-123"), 3),
        Ramo("INF-214", "Expr. Oral", 3, true, listOf("INF-124"), 3),
        Ramo("INF-215", "Circuitos Digitales", 6, true, listOf("INF-111"), 3),
        Ramo("IFG-300", "Inglés III", 3, true, listOf("IFG-200"), 3),

        // SEMESTRE 4
        Ramo("INF-221", "Física II", 6, false, listOf("INF-211"), 4),
        Ramo("INF-222", "Ecuaciones Dif.", 5, false, listOf("INF-212"), 4),
        Ramo("INF-223", "Progra. Avanzada", 5, false, listOf("INF-213"), 4),
        Ramo("INF-224", "Lógica Comp.", 4, true, listOf("INF-111"), 4),
        Ramo("INF-225", "Arq. Computadores", 5, true, listOf("INF-215"), 4),
        Modulos("INF-226", "Modulo integrador (Hito I)", 100, listOf("INF-212", "INF-213", "INF-215"), 4),

        // SEMESTRE 5
        Ramo("INF-311", "Física III", 5, false, listOf("INF-221"), 5),
        Ramo("INF-312", "Inf. Estadística", 6, true, listOf("INF-222"), 5),
        Ramo("INF-313", "Estr. Discretas", 5, true, listOf("INF-223"), 5),
        Ramo("INF-314", "Modelamiento Datos", 5, true, listOf("INF-223"), 5),
        Ramo("INF-315", "Taller Circuitos", 4, true, listOf("INF-225"), 5),
        Ramo("MFG-114", "Intr. a la Fe", 5, true, emptyList(), 5),

        // SEMESTRE 6
        Ramo("INF-321", "Comp. Numérica", 4, true, listOf("INF-222"), 6),
        Ramo("INF-322", "Inv. Operaciones", 5, true, listOf("INF-312"), 6),
        Ramo("INF-323", "Teoría Comp.", 5, true, listOf("INF-224"), 6),
        Ramo("INF-324", "Base de Datos", 6, true, listOf("INF-314"), 6),
        Ramo("INF-325", "Sist. Operativos", 5, true, listOf("INF-315"), 6),
        Ramo("MFG-216", "Ética Cristiana", 5, true, listOf("MFG-114"), 6),

        // SEMESTRE 7
        Ramo("INF-411", "Metodología Inv.", 6, true, listOf("INF-226"), 7),
        Ramo("INF-412", "Economía", 6, true, listOf("INF-226"), 7),
        Ramo("INF-413", "Algoritmos", 6, true, listOf("INF-313"), 7),
        Ramo("INF-414", "Sist. Información", 6, true, listOf("INF-324"), 7),
        Ramo("INF-415", "Redes y Datos", 6, true, listOf("INF-325"), 7),

        // SEMESTRE 8
        Modulos("INF-421", "Mód. Int. Licenciatura", 200, listOf("INF-411","INF-412","INF-413", "INF-414", "INF-415"), 8),
        Ramo("INF-422", "Contab. y Finanzas", 4, true, listOf("INF-412"), 8),
        Ramo("INF-423", "Int. Artificial", 4, true, listOf("INF-413"), 8),
        Ramo("INF-424", "Ing. Software I", 5, true, listOf("INF-414"), 8),
        Ramo("INF-425", "Redes Avanzadas", 4, true, listOf("INF-415"), 8),
        Ramo("CFG-1",   "Certificación I", 5, true, emptyList(), 8),

        // SEMESTRE 9
        Ramo("INF-511", "Eval. Proyectos", 5, true, listOf("INF-422"), 9),
        Ramo("INF-512", "Gestión Inf. I", 5, true, listOf("INF-422"), 9),
        Ramo("INF-513", "Calidad SW", 5, true, listOf("INF-424"), 9),
        Ramo("INF-514", "Ing. Software II", 5, true, listOf("INF-424"), 9),
        Ramo("INF-515", "Sist. Distribuidos", 5, true, listOf("INF-415"), 9),
        Ramo("CFG-2",   "Certificación II", 5, true, listOf("CFG-1"), 9),

        // SEMESTRE 10
        Ramo("INF-521", "RRHH y Legislación", 5, true, emptyList(), 10),
        Ramo("INF-522", "Gestión Inf. II", 5, true, listOf("INF-512"), 10),
        Ramo("INF-523", "Electivo I", 5, true, emptyList(), 10),
        Ramo("INF-524", "Taller Des. SW", 5, true, listOf("INF-514"), 10),
        Ramo("INF-525", "Seguridad Inf.", 5, true, listOf("INF-425"), 10),
        Ramo("CFG-3",   "Certificación III", 5, true, listOf("CFG-2"), 10),

        // SEMESTRE 11
        Ramo("INF-611", "Electivo II", 5, true, listOf("INF-421"), 11),
        Ramo("INF-612", "Electivo III", 5, true, listOf("INF-421"), 11),
        // La Práctica: Requiere haber acumulado casi toda la carrera
        Modulos("INF-613", "Práctica Profesional", 300, listOf("INF-524", "INF-522"), 11)
    )
}