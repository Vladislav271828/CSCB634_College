import { useContext, useEffect, useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import UserContext from '../Context/UserProvider';

const GradePage = ({ title,
    selectListValues,
    backFunc
}) => {

    const [fetchedEditValues, setFetchedEditValues] = useState({});
    const [students, setStudents] = useState({});
    const [gradeFields, setGradeFields] = useState({});
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);
    const [loading, setLoading] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const { id } = useContext(UserContext);

    const handleGradeChange = async (studentId, gradeId, target) => {
        setErrMsg("");
        try {

            await axiosPrivate.post("/enrollmentGrade/professor/create", {
                enrollmentId: fetchedEditValues[studentId].id,
                gradeId,
                score: target.value
            });

            target.className = "recently-changed-grade"

        } catch (err) {
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    };

    const handleFinalChange = async (studentId, target) => {
        setErrMsg("");
        try {

            await axiosPrivate.get(`/enrollment/professor/finalGrade/${fetchedEditValues[studentId].id}/${target.value ? target.value : 0}`);

            target.className = "recently-changed-grade"

        } catch (err) {
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    };

    const handleBack = () => {
        if (backFunc) backFunc();
        else
            navigate("../", { relative: "path" });
    }

    useEffect(() => {
        fetchEditValues()
    }, [])

    const fetchEditValues = async () => {
        setLoading(true)
        try {
            const year = Number.isInteger(selectListValues.year) ? selectListValues.year : selectListValues.year.slice(0, 4)

            const editres = await axiosPrivate.get(`/enrollment/getAllByProfessorIdAndYearAndCourse/${id}/${year}/${selectListValues.courseId}`);
            const gradeFieldRes = await axiosPrivate.get(`/grade/getAllByEnrollment/${selectListValues.courseId}/${year}`);
            const studentRes = await axiosPrivate.get(`/student/professor/getAllStudentsByCourseId/${selectListValues.courseId}`);

            const studentEnrollmentMap = editres.data.reduce((acc, item) => {
                acc[item.studentId] = { id: item.id, finalGrade: item.finalGrade ? item.finalGrade : undefined };
                item?.grades.forEach(grade => {
                    acc[item.studentId] = { ...acc[item.studentId], [grade.id.gradeId]: grade.score ? grade.score : undefined }
                });
                return acc
            }, {})
            setFetchedEditValues(studentEnrollmentMap);
            setGradeFields(gradeFieldRes.data);
            setStudents(studentRes.data)
            setLoading(false)
        } catch (err) {
            setSuccess(false)
            setLoading(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    }

    return (
        <div className='component-container'>
            {title ? <h1>{title}</h1> : <></>}
            {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}
            {loading && <p>Loading...</p>}
            {!loading && <form className='form-structure-container' onSubmit={(e) => { e.preventDefault }}>
                <main>
                    <table style={{
                        minWidth: ((gradeFields.length + 1) * 60 + 150) + "px",
                        maxWidth: ((gradeFields.length + 1) * 60 + 300) + "px",
                    }} className="tg grade-table"><colgroup>
                            <col></col>
                            {gradeFields.map((grade) => { return <col key={grade.id} className='grade-col'></col> })}
                            <col className='grade-col'></col>
                        </colgroup><thead>
                            <tr>
                                <th className="tg-g6sd">Student</th>
                                {gradeFields.map((field) => {
                                    return <th key={field.id} className="tg-kiqf">{field.name}</th>
                                })}
                                <th className="tg-kiqf">Final Grade</th>
                            </tr></thead>
                        <tbody>
                            {students.sort((a, b) => {
                                return (a.firstname + a.lastname).localeCompare(b.firstname + b.lastname)
                            }).map((student) => {
                                return <tr key={student.id}>
                                    <td className="tg-0pky">{student.firstname + " " + student.lastname}</td>
                                    {gradeFields.map((grade) => {
                                        return <td key={grade.id} className="tg-0pkx">
                                            <select
                                                name={student.id + "/" + grade.id}
                                                onChange={(e) => handleGradeChange(student.id, grade.id, e.target)}
                                                defaultValue={fetchedEditValues[student.id][grade.id]}
                                            >
                                                <option value={null}></option>
                                                <option value={200}>2.00</option>
                                                <option value={300}>3.00</option>
                                                <option value={350}>3.50</option>
                                                <option value={400}>4.00</option>
                                                <option value={450}>4.50</option>
                                                <option value={500}>5.00</option>
                                                <option value={550}>5.50</option>
                                                <option value={600}>6.00</option>
                                            </select>
                                        </td>
                                    })}
                                    <td className="tg-1wig">
                                        <select
                                            name={student.id}
                                            onChange={(e) => handleFinalChange(student.id, e.target)}
                                            defaultValue={fetchedEditValues[student.id].finalGrade}
                                        >
                                            <option value={null}></option>
                                            <option value={200}>2.00</option>
                                            <option value={300}>3.00</option>
                                            <option value={350}>3.50</option>
                                            <option value={400}>4.00</option>
                                            <option value={450}>4.50</option>
                                            <option value={500}>5.00</option>
                                            <option value={550}>5.50</option>
                                            <option value={600}>6.00</option>
                                        </select></td>
                                </tr>
                            })}
                        </tbody>
                    </table>
                </main>
                <div className="btn table-back-btn">
                    <button type='button' onClick={() => handleBack()}>
                        Go Back
                    </button>
                </div>
            </form>}
        </div>

    )
}

export default GradePage