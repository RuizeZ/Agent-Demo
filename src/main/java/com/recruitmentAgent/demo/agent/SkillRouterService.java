package com.recruitmentAgent.demo.agent;



import com.recruitmentAgent.demo.service.QwenService;
import com.recruitmentAgent.demo.skill.Skill;
import com.recruitmentAgent.demo.skill.SkillRegistry;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class SkillRouterService {

    private final SkillRegistry skillRegistry;
    private final QwenService qwenService;
    private final ObjectMapper objectMapper;

    public SkillRouterService(
            SkillRegistry skillRegistry,
            QwenService qwenService,
            ObjectMapper objectMapper
    ) {
        this.skillRegistry = skillRegistry;
        this.qwenService = qwenService;
        this.objectMapper = objectMapper;
    }

    public SkillSelection selectSkill(String userInput) {
        String prompt = buildPrompt(userInput);

        String aiResponse = qwenService.call(prompt);

        String cleanedResponse = cleanJson(aiResponse);

        try {
            return objectMapper.readValue(
                    cleanedResponse,
                    SkillSelection.class
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "无法解析千问返回的 Skill 选择结果："
                            + aiResponse,
                    e
            );
        }
    }

    private String buildPrompt(String userInput) {
        StringBuilder skillDescription = new StringBuilder();

        for (Skill skill : skillRegistry.listSkills()) {
            skillDescription
                    .append("- name: ")
                    .append(skill.name())
                    .append("\n")
                    .append("  description: ")
                    .append(skill.description())
                    .append("\n");
        }

        return """
                你是招聘系统的 Skill 路由器。

                你的任务是根据用户输入，从可用 Skill 中选择一个最合适的 Skill。

                可用 Skill：
                %s

                用户输入：
                %s

                只返回合法 JSON，不要输出 Markdown，不要解释。

                返回格式：
                {
                  "skill": "Skill名称",
                  "input": "传给该Skill的完整输入"
                }

                规则：
                1. skill 必须是可用 Skill 中存在的名称。
                2. input 应保留用户的重要条件。
                3. 搜索职位时选择 search_job。
                4. 推荐候选人时选择 recommend_candidate。
                """.formatted(skillDescription, userInput);
    }

    private String cleanJson(String response) {
        return response
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
